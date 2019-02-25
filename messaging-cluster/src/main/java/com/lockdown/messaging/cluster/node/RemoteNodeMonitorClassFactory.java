package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;

public interface RemoteNodeMonitorClassFactory {

    public RemoteNodeMonitor getInstance(MessagingNodeContext context);

}
