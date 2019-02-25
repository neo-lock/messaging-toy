package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;

public class DefaultLocalServerNodeFactory implements LocalServerNodeFactory {

    @Override
    public LocalServerNode getInstance(MessagingNodeContext nodeContext) {
        return new DefaultLocalServerNode(nodeContext.getRemoteNodeMonitor(),nodeContext.getLocalDestination());
    }

}
