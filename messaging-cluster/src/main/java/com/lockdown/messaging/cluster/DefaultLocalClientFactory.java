package com.lockdown.messaging.cluster;

public class DefaultLocalClientFactory implements LocalClientFactory {
    @Override
    public LocalClient getInstance(MessagingNodeContext nodeContext) {
        return new ClusterLocalClient(nodeContext);
    }
}
