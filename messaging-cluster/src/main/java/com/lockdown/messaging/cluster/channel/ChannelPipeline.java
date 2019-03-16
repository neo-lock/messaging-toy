package com.lockdown.messaging.cluster.channel;

public interface ChannelPipeline extends ChannelInboundInvoker, ChannelOutboundInvoker, ChannelInvoker {


    ChannelPipeline addLast(ChannelHandler handler);

    Channel channel();

    void writeAndFlush(Object message);

}
