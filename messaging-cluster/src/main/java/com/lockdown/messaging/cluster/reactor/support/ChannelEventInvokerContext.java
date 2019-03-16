package com.lockdown.messaging.cluster.reactor.support;

public interface ChannelEventInvokerContext extends ChannelEventInvoker {

    void registerEventInvoker(ChannelTypeEventInvoker eventInvoker);
}
