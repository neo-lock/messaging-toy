package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;

public interface LocalServerNodeFactory {


    LocalServerNode getInstance(MessagingNodeContext nodeContext);
}
