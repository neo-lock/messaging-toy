package com.lockdown.messaging.cluster.channel;

public interface LocalChannelContext extends ChannelContext {

    LocalChannelHandler handler();

    LocalChannelPipeline pipeline();


}
