package com.lockdown.messaging.cluster.channel;

public interface ChannelHandler {

    void exceptionCaught(ChannelContext ctx, Throwable throwable);

    void channelClosed(ChannelContext ctx);


}
