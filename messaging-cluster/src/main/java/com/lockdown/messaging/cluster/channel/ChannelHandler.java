package com.lockdown.messaging.cluster.channel;

public interface ChannelHandler {



    void channelReceived(MessagingChannelContext ctx, Object message);


    void channelRegistered(MessagingChannelContext ctx);

    void channelClosed(MessagingChannelContext ctx);


    void exceptionCaught(MessagingChannelContext ctx, Throwable throwable);

}
