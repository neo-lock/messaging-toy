package com.lockdown.messaging.cluster.channel;

public interface ChannelPipeline<T extends ChannelHandler> extends ChannelInBound {


    ChannelPipeline addLast(T handler);


    Channel channel();

}
