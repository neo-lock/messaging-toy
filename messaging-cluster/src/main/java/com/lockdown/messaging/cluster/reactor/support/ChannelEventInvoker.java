package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.reactor.ChannelEvent;

public interface ChannelEventInvoker {

    public void handleEvent(ChannelEvent event);
}
