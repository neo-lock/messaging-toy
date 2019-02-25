package com.lockdown.messaging.cluster;


public interface LocalClientFactory {

    public LocalClient getInstance(MessagingNodeContext nodeContext);
}
