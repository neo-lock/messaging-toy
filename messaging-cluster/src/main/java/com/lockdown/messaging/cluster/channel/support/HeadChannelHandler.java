package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandlerAdapter;
import com.lockdown.messaging.cluster.channel.ChannelOutboundHandler;

public class HeadChannelHandler  extends ChannelInboundHandlerAdapter implements ChannelOutboundHandler {
    private final Channel channel;

    public HeadChannelHandler(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void channelWrite(ChannelContext ctx, Object message) {
        channel.writeAndFlush(message);
    }
}
