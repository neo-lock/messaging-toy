package com.lockdown.messaging.cluster.node;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.LocalClient;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandExecutor;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandInvoker;
import com.lockdown.messaging.cluster.sockethandler.ServerNodeFactory;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRemoteNodeMonitor implements RemoteNodeMonitor,NodeCommandExecutor<DefaultRemoteNodeMonitor> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<ServerDestination,RemoteServerNode> nodeContext = new ConcurrentHashMap<>();
    private LocalClient localClient;
    private CommandAcceptor commandAcceptor;
    private Object lock = new Object();
    private Map<CommandType,NodeCommandInvoker<DefaultRemoteNodeMonitor>> invokeContext = new HashMap<>();


    public DefaultRemoteNodeMonitor(LocalClient client) {
        this.localClient = client;
        this.initInvokeContext();
    }

    private void initInvokeContext(){
        registerInvoke(new NodeClosedMonitor());
    }

    private void registerInvoke(NodeCommandInvoker<DefaultRemoteNodeMonitor> invoker){
        this.invokeContext.putIfAbsent(invoker.supportType(),invoker);
    }


    @Override
    public RemoteServerNode getRemoteNode(ServerDestination destination){
        if(destination.equals(localClient.localDestination())){
            throw new UnsupportedOperationException(" 不用连接自己!");
        }
        RemoteServerNode serverNode = null;
        synchronized (lock){
            if(!nodeContext.containsKey(destination)){
                ChannelFuture channelFuture = localClient.connect(destination);
                serverNode = ServerNodeFactory.getRemoteNodeInstance(channelFuture.channel(),destination);
                addRemoteNode(serverNode);
                return serverNode;
            }else{
                return nodeContext.get(destination);
            }
        }
    }

    private void addRemoteNode(RemoteServerNode remoteServerNode){
        logger.info(" 添加一个远程节点 {}",remoteServerNode.destination());
        nodeContext.putIfAbsent(remoteServerNode.destination(),remoteServerNode);
        logger.info(" current nodes {}", JSON.toJSON(nodeContext));
    }

    private RemoteServerNode removeRemoteNode(ServerDestination destination){
        logger.info(" 移除一个远程节点 {}",destination);
        if(Objects.nonNull(destination)){
            RemoteServerNode node = nodeContext.remove(destination);
            logger.info(" current nodes {}", JSON.toJSON(nodeContext));
            return node;
        }else{
            return null;
        }
    }



    @Override
    public Collection<RemoteServerNode> remoteNodes() {
        return nodeContext.values();
    }

    @Override
    public void registerCommandHandler(CommandAcceptor acceptor) {
        this.commandAcceptor = acceptor;
    }

    @Override
    public void commandForward(RemoteServerNode remoteServerNode, NodeCommand command) {
        if(Objects.isNull(commandAcceptor)){
            throw new IllegalStateException(" command acceptor not set !");
        }
        commandAcceptor.commandEvent(remoteServerNode,command);
    }


    @Override
    public void nodeRegistered(RemoteServerNode remoteServerNode) {
        if(Objects.isNull(remoteServerNode.destination())){
            throw new IllegalStateException(" remote server node destination cannot be empty !");
        }
        addRemoteNode(remoteServerNode);
        commandAcceptor.commandEvent(remoteServerNode,new NodeRegister(remoteServerNode.destination()));
    }


    @Override
    public void inactive(ServerDestination destination) {
        RemoteServerNode node = removeRemoteNode(destination);
        if(Objects.nonNull(node)){
            commandAcceptor.commandEvent(node,new NodeClosed());
        }
    }


    @Override
    public void commandEvent(RemoteServerNode node, NodeCommand command) {
        executeCommand(this,node,command);
    }

    @Override
    public void executeCommand(DefaultRemoteNodeMonitor invoke, RemoteServerNode remote, NodeCommand command) {
        if(invokeContext.containsKey(command.type())){
            invokeContext.get(command.type()).executeCommand(invoke,remote,command);
        }
        commandForward(remote,command);
    }


    public class NodeClosedMonitor implements NodeCommandInvoker<DefaultRemoteNodeMonitor>{

        @Override
        public CommandType supportType() {
            return CommandType.CLOSED;
        }

        @Override
        public void executeCommand(DefaultRemoteNodeMonitor invoke, RemoteServerNode remote, NodeCommand command) {
            removeRemoteNode(remote.destination());
        }
    }


}
