package com.lockdown.messaging.cluster.channel;

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
