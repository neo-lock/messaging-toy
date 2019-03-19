package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelOutboundHandler;

public class ChannelOutboundHandlerAdapter implements ChannelOutboundHandler {


    @Override
    public void exceptionCaught(ChannelContext ctx, Throwable throwable) {
        ctx.fireExceptionCaught(throwable);
    }

    @Override
    public void channelClosed(ChannelContext ctx) {
        ctx.fireChannelClosed();
    }

    @Override
    public void channelWrite(ChannelContext ctx, Object message) {
        ctx.fireChannelWrite(message);
    }
}
