package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.channel.MessagingChannelFactory;
import io.netty.channel.ChannelHandlerContext;

public interface ReactorContext extends MessagingChannelFactory {


    void registerChannel(ChannelHandlerContext ctx);


    void channelEvent(ChannelEvent event);


}
