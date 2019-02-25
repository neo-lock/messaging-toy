package com.lockdown.messaging.core;

import io.netty.channel.ChannelHandlerContext;

public interface MessageProcessor {


    public void processMessage(ChannelHandlerContext ctx, Object message);

}
