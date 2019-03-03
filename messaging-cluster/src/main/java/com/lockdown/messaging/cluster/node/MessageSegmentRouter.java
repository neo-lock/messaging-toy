package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ContextExecutor;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.NodeMessageAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MessageSegmentRouter implements NodeMessageRouter, NodeMessageAcceptor {
    private final ClusterNodeMonitor nodeMonitor;
    private final ContextExecutor executor;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private NodeMessageAcceptor acceptor;


    public MessageSegmentRouter(ClusterNodeMonitor nodeMonitor, ContextExecutor executor) {
        this.executor = executor;
        this.nodeMonitor = nodeMonitor;
        this.nodeMonitor.registerAcceptor(this);
    }

    @Override
    public void notifyMessage(SourceNodeCommand command, ServerDestination... ignores) {
        Set<ServerDestination> whiteList = new HashSet<>(Arrays.asList(ignores));
        Collection<RemoteNode> nodes = nodeMonitor.AllDestination();
        nodes.forEach(remoteServerNode -> {
            if (whiteList.contains(remoteServerNode.destination())) {
                return;
            }
            remoteServerNode.writeMessage(command);
        });
    }

    @Override
    public void sendRandomTarget(SourceNodeCommand command) {
        RemoteNode random = nodeMonitor.randomNode();
        random.writeMessage(command);
    }


    @Override
    public void acceptedMessage(RemoteNode remoteNode, SourceNodeCommand message) {
        executor.getSegment().execute(() -> messageTriggered(remoteNode, message));
    }

    @Override
    public void sendMessage(ServerDestination destination, SourceNodeCommand message) {
        RemoteNode serverNode = nodeMonitor.findByDestination(destination);
        serverNode.writeMessage(message);
    }

    @Override
    public void messageTriggered(RemoteNode channelSlot, SourceNodeCommand message) {
        acceptor.acceptedMessage(channelSlot, message);
    }

    @Override
    public void registerAcceptor(NodeMessageAcceptor acceptor) {
        this.acceptor = acceptor;
    }

}
