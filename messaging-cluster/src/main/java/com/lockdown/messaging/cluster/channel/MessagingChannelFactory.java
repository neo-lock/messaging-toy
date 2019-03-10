package com.lockdown.messaging.cluster.channel;

import io.netty.channel.ChannelHandlerContext;


public interface MessagingChannelFactory {

    public MessagingChannel newInstance(ChannelHandlerContext ctx);

}
