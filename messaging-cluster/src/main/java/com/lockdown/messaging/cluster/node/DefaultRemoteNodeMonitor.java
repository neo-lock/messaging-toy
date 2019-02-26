package com.lockdown.messaging.cluster.node;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandExecutor;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandInvoker;
import com.lockdown.messaging.cluster.sockethandler.ServerNodeFactory;
import com.lockdown.messaging.cluster.utils.GlobalTimer;
import io.netty.channel.ChannelFuture;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DefaultRemoteNodeMonitor implements RemoteNodeMonitor, NodeCommandExecutor<DefaultRemoteNodeMonitor> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private MessagingNodeContext messagingNodeContext;
    private Map<ServerDestination, RemoteServerNode> nodeContext = new ConcurrentHashMap<>();
    private Map<CommandType, NodeCommandInvoker<DefaultRemoteNodeMonitor>> invokeContext = new HashMap<>();
    private CommandAcceptor commandAcceptor;
    private Object lock = new Object();


    DefaultRemoteNodeMonitor(MessagingNodeContext nodeContext) {
        this.messagingNodeContext = nodeContext;
        this.initInvokeContext();
    }

    private void initInvokeContext() {
        registerInvoke(new NodeClosedMonitor());
    }

    private void registerInvoke(NodeCommandInvoker<DefaultRemoteNodeMonitor> invoker) {
        this.invokeContext.putIfAbsent(invoker.supportType(), invoker);
    }


    @Override
    public RemoteServerNode getRemoteNode(ServerDestination destination) {
        if (destination.equals(messagingNodeContext.getLocalDestination())) {
            throw new UnsupportedOperationException(" 不用连接自己!");
        }
        RemoteServerNode serverNode = null;
        synchronized (lock){
            if (!nodeContext.containsKey(destination)) {
                ChannelFuture channelFuture = messagingNodeContext.connect(destination);
                serverNode = ServerNodeFactory.getRemoteNodeInstance(channelFuture, destination);
                addRemoteNode(serverNode);
            } else {
                serverNode =  nodeContext.get(destination);
            }
        }
        return serverNode;
    }



    private void addRemoteNode(RemoteServerNode remoteServerNode) {
        logger.info(" 添加一个远程节点 {}", remoteServerNode.destination());
        nodeContext.putIfAbsent(remoteServerNode.destination(), remoteServerNode);
        logger.info(" current nodes {}", JSON.toJSON(nodeContext));
    }

    private RemoteServerNode removeRemoteNode(ServerDestination destination) {
        logger.info(" 移除一个远程节点 {}", destination);
        if (Objects.nonNull(destination)) {
            RemoteServerNode node = nodeContext.remove(destination);
            logger.info(" current nodes {}", JSON.toJSONString(nodeContext));
            return node;
        } else {
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
        if (Objects.isNull(commandAcceptor)) {
            throw new IllegalStateException(" command acceptor not set !");
        }
        commandAcceptor.commandEvent(remoteServerNode, command);
    }

    @Override
    public RemoteServerNode randomNode() {
        if(nodeContext.size()==0){
            return null;
        }else {
            return  nodeContext.values().stream().findFirst().orElse(null);
        }
    }

    @Override
    public void printNodes() {
        logger.info("当前节点信息:{}",JSON.toJSONString(nodeContext.keySet()));
    }


    @Override
    public void nodeRegistered(RemoteServerNode remoteServerNode, NodeCommand command) {
        if (Objects.isNull(remoteServerNode.destination())) {
            throw new IllegalStateException(" remote server node destination cannot be empty !");
        }
        addRemoteNode(remoteServerNode);
    }


    @Override
    public void inactive(ServerDestination destination) {
        RemoteServerNode node = removeRemoteNode(destination);
        if (Objects.nonNull(node)) {
            commandForward(node, new NodeClosed(node.destination()));
        }
    }


    @Override
    public void commandEvent(RemoteServerNode node, NodeCommand command) {
        executeCommand(this, node, command);
    }

    @Override
    public void executeCommand(DefaultRemoteNodeMonitor invoke, RemoteServerNode remote, NodeCommand command) {
        if (invokeContext.containsKey(command.type())) {
            invokeContext.get(command.type()).executeCommand(invoke, remote, command);
        }
        messagingNodeContext.executeRunnable(() -> commandForward(remote, command));
    }

    @Override
    public void serverStartup(LocalServer localServer, MessagingNodeContext properties) {
        //ignore
    }

    @Override
    public void serverStop(LocalServer localServer) {
        nodeContext.values().forEach(RemoteServerNode::close);
    }


    public class NodeClosedMonitor implements NodeCommandInvoker<DefaultRemoteNodeMonitor> {

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
