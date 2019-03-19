package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandler;

public class ChannelInboundHandlerAdapter implements ChannelInboundHandler {


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ctx.fireChannelReceived(message);
    }

    @Override
    public void exceptionCaught(ChannelContext ctx, Throwable throwable) {
        ctx.fireExceptionCaught(throwable);
    }

    @Override
    public void channelClosed(ChannelContext ctx) {
        ctx.fireChannelClosed();
    }
}
