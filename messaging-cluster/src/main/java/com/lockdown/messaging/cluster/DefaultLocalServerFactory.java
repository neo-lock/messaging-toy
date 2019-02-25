package com.lockdown.messaging.cluster;

public class DefaultLocalServerFactory implements LocalServerFactory {
    @Override
    public LocalServer getInstance(MessagingNodeContext nodeContext) {
        return new ClusterLocalServer(nodeContext);
    }
}
