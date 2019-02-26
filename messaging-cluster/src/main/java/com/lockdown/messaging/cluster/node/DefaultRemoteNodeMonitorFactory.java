package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;

public class DefaultRemoteNodeMonitorFactory implements RemoteNodeMonitorFactory {
    @Override
    public RemoteNodeMonitor getInstance(MessagingNodeContext context) {
        return new DefaultRemoteNodeMonitor(context);
    }
}
