package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelHandler;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public class DefaultMessagingChannelContext extends AbstractMessagingChannelContext {

    private final ChannelHandler channelHandler;

    DefaultMessagingChannelContext(ChannelEventLoop eventLoop, ChannelHandler channelHandler) {
        super(eventLoop);
        this.channelHandler = channelHandler;
    }


    @Override
    public ChannelHandler handler() {
        return channelHandler;
    }
}
