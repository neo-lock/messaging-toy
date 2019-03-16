package com.lockdown.messaging.cluster.channel;

public interface ChannelInboundHandler extends ChannelHandler {

    void channelReceived(ChannelContext ctx, Object message);


}
