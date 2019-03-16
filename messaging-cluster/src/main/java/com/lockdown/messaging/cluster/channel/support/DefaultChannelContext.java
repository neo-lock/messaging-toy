package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelHandler;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandler;
import com.lockdown.messaging.cluster.channel.ChannelOutboundHandler;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;

public class DefaultChannelContext extends AbstractChannelContext {

    private final ChannelHandler handler;

    DefaultChannelContext(ChannelPipeline pipeline, ChannelHandler handler) {
        super(pipeline, isInbound(handler), isOutbound(handler));
        this.handler = handler;
    }

    private static boolean isInbound(ChannelHandler handler) {
        return handler instanceof ChannelInboundHandler;
    }

    private static boolean isOutbound(ChannelHandler handler) {
        return handler instanceof ChannelOutboundHandler;
    }

    @Override
    public ChannelHandler handler() {
        return handler;
    }


}
