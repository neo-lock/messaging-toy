package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.exception.MessagingNodeNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractNodeMonitor implements NodeMonitor {


    protected Map<ServerDestination, RemoteNode> nodeContext = new ConcurrentHashMap<>();


    @Override
    public RemoteNode getRemoteNode(ServerDestination destination) {
        Objects.requireNonNull(destination);
        if (nodeContext.containsKey(destination)) {
            throw new MessagingNodeNotFoundException(destination);
        }
        return nodeContext.get(destination);
    }

    @Override
    public Collection<RemoteNode> AllRemoteNodes() {
        return nodeContext.values();
    }

    @Override
    public RemoteNode randomNode() {
        if (nodeContext.size() == 0) {
            throw new MessagingNodeNotFoundException("没有更多节点!");
        }
        return getFirstNode();
    }


    private RemoteNode getFirstNode() {
        Optional<RemoteNode> optional = nodeContext.values().stream().findFirst();
        if (!optional.isPresent()) {
            throw new MessagingNodeNotFoundException("没有更多节点!");
        }
        return optional.get();
    }


}
