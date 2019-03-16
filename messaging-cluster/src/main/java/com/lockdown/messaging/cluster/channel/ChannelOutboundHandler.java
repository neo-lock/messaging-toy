package com.lockdown.messaging.cluster.channel;

public interface ChannelOutboundHandler extends ChannelHandler {

    void channelWrite(ChannelContext ctx, Object message);

}
