package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.framwork.Destination;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import io.netty.channel.ChannelHandlerContext;

public interface MessagingChannel {

    MessagingChannelPipeline pipeline();

    ChannelHandlerContext nettyChannelContext();

    Destination destination();

    void handleEvent(ChannelEvent channelEvent);
}
