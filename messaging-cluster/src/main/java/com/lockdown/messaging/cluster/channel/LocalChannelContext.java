package com.lockdown.messaging.cluster.channel;

import io.netty.util.concurrent.EventExecutor;

public interface LocalChannelContext extends ChannelContext {

    LocalChannelHandler handler();

    LocalChannelPipeline pipeline();



}
