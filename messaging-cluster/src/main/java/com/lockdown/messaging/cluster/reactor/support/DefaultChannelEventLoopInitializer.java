package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.channel.ChannelEventLoopInitializer;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public class DefaultChannelEventLoopInitializer implements ChannelEventLoopInitializer<ChannelEventLoop> {


    @Override
    public void initialize(ChannelEventLoop eventLoop) {
        eventLoop.eventInvokerContext().registerEventInvoker(new LocalChannelEventInvoker(eventLoop.localChannel()));
        eventLoop.eventInvokerContext().registerEventInvoker(new NodeChannelEventInvoker(eventLoop.nodeChannelGroup()));
    }
}
