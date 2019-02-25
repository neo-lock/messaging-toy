package com.lockdown.messaging.cluster;

public interface LocalServerFactory {

    LocalServer getInstance(MessagingNodeContext nodeContext);
}
