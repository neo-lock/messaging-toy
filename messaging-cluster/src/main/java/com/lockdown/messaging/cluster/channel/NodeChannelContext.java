package com.lockdown.messaging.cluster.channel;

public interface NodeChannelContext extends NodeChannelInbound, ChannelContext {

    NodeChannelHandler handler();


    NodeChannelPipeline pipeline();

}
