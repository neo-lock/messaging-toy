package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.RegisterNature;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


public class DefaultNodeMonitor extends AbstractNodeMonitor implements NodeMonitorSlot,RemoteNodeBeanFactory {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<ChannelId, RemoteNode> invalidNodes = new ConcurrentHashMap<>();
    private final RemoteMonitoringNodeBeanFactory beanFactory;
    private CommandAcceptor commandAcceptor;
    private Object lock = new Object();

    DefaultNodeMonitor(RemoteMonitoringNodeBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beanFactory.registerNodeMonitorSlot(this);
    }

    @Override
    public RemoteNode getRemoteNode(ServerDestination destination) {
        Objects.requireNonNull(destination);
        RemoteNode remoteNode;
        synchronized (lock) {
            if (nodeContext.containsKey(destination)) {
                remoteNode = nodeContext.get(destination);
            } else {
                remoteNode = beanFactory.getNodeInstance(destination);
                nodeContext.putIfAbsent(remoteNode.destination(), remoteNode);
            }
        }
        return remoteNode;
    }

    @Override
    public void shutdown() {
        nodeContext.values().forEach(RemoteNode::close);
    }

    @Override
    public void printNodes() {
        logger.debug("当前节点 {}",nodeContext.keySet());
    }


    @Override
    public void receivedCommand(RemoteNode remoteNode, SourceNodeCommand command) {
        if (RegisterNature.class.isAssignableFrom(command.getClass())) {
            effectiveNode(remoteNode,command.getSource());
        }
        commandTriggered(remoteNode, command);
    }

    @Override
    public void inactive(RemoteNode remoteNode) {
        Objects.requireNonNull(remoteNode);
        nodeContext.remove(remoteNode.destination());
        remoteNode.close();
        receivedCommand(remoteNode, new NodeClosed(remoteNode.destination()));
    }

    @Override
    public void exceptionCaught(RemoteNode remoteNode, Throwable cause) {
        inactive(remoteNode);
    }

    @Override
    public void commandTriggered(RemoteNode node, SourceNodeCommand command) {
        this.commandAcceptor.acceptedCommand(node, command);
    }

    @Override
    public void registerAcceptor(CommandAcceptor acceptor) {
        this.commandAcceptor = acceptor;
    }

    private void effectiveNode(RemoteNode remoteNode,ServerDestination destination){
        Objects.requireNonNull(destination);
        remoteNode.applyDestination(destination);
        ChannelId channelId = remoteNode.channelId();
        if (!invalidNodes.containsKey(channelId)) {
            return;
        }
        RemoteNode node = invalidNodes.remove(channelId);
        node.applyDestination(destination);
        nodeContext.putIfAbsent(destination, remoteNode);
    }

    @Override
    public RemoteNode getNodeInstance(ChannelFuture channelFuture, ServerDestination destination) {
        RemoteNode remoteNode = beanFactory.getNodeInstance(channelFuture,destination);
        if(Objects.isNull(destination)){
            invalidNodes.put(remoteNode.channelId(),remoteNode);
        }else{
            nodeContext.putIfAbsent(destination,remoteNode);
        }
        return remoteNode;
    }

    @Override
    public RemoteNode getNodeInstance(ServerDestination destination) {
        RemoteNode remoteNode =  beanFactory.getNodeInstance(destination);
        nodeContext.putIfAbsent(destination,remoteNode);
        return remoteNode;
    }
}
