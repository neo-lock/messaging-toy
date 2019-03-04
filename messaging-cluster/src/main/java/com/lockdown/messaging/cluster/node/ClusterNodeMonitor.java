package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.RegisterNature;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.exception.MessagingDestinationNotFoundException;
import com.lockdown.messaging.cluster.framwork.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ClusterNodeMonitor extends AbstractChannelSlotMonitor<RemoteNode, ServerDestination, SourceNodeCommand>
        implements ClusterNodeBeanFactory, NodeSlotMonitor {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<ChannelId, RemoteNode> invalidNodes = new ConcurrentHashMap<>();
    private AbstractClusterNodeBeanFactory beanFactory;
    private NodeMessageAcceptor messageAcceptor;
    private Object lock = new Object();

    public ClusterNodeMonitor(AbstractClusterNodeBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        beanFactory.setMonitorUnit(this);
    }


    @Override
    public RemoteNode findByDestination(ServerDestination destination) {
        Objects.requireNonNull(destination);
        RemoteNode remoteNode;
        synchronized (lock) {
            if (destinationContext.containsKey(destination)) {
                remoteNode = destinationContext.get(destination);
            } else {
                remoteNode = getInstance(destination);
            }
        }
        return remoteNode;
    }

    @Override
    public void inactive(RemoteNode slot) {
        super.inactive(slot);
        messageTriggered(slot, new NodeClosed(slot.destination()));
    }


    @Override
    public void shutdown() {
        destinationContext.values().forEach(ChannelSlot::close);
    }

    public RemoteNode randomNode() {
        if (destinationContext.size() == 0) {
            throw new MessagingDestinationNotFoundException("没有更多节点!");
        }
        return getFirstNode();
    }

    private RemoteNode getFirstNode() {
        Optional<RemoteNode> optional = destinationContext.values().stream().findFirst();
        if (!optional.isPresent()) {
            throw new MessagingDestinationNotFoundException("没有更多节点!");
        }
        return optional.get();
    }


    @Override
    public RemoteNode getInstance(ChannelFuture channelFuture, ServerDestination destination) {
        RemoteNode remoteNode = beanFactory.getInstance(channelFuture, destination);
        if (Objects.isNull(destination)) {
            invalidNodes.put(remoteNode.channelId(), remoteNode);
        } else {
            destinationContext.putIfAbsent(destination, remoteNode);
        }
        return remoteNode;
    }

    @Override
    public RemoteNode getInstance(ServerDestination destination) {
        RemoteNode remoteNode = beanFactory.getInstance(destination);
        destinationContext.putIfAbsent(destination, remoteNode);
        return remoteNode;
    }


    private void effectiveNode(RemoteNode remoteNode, ServerDestination destination) {
        Objects.requireNonNull(destination);
        remoteNode.applyDestination(destination);
        ChannelId channelId = remoteNode.channelId();
        if (!invalidNodes.containsKey(channelId)) {
            return;
        }
        RemoteNode node = invalidNodes.remove(channelId);
        node.applyDestination(destination);
        destinationContext.putIfAbsent(destination, remoteNode);
    }


    @Override
    public void messageTriggered(RemoteNode remoteNode, SourceNodeCommand message) {
        messageAcceptor.acceptedMessage(remoteNode, message);
    }

    @Override
    public void registerAcceptor(NodeMessageAcceptor acceptor) {
        this.messageAcceptor = acceptor;
    }

    @Override
    public void acceptedMessage(RemoteNode remoteNode, SourceNodeCommand message) {
        if (RegisterNature.class.isAssignableFrom(message.getClass())) {
            effectiveNode(remoteNode, message.getSource());
        }
        messageTriggered(remoteNode, message);
    }
}
