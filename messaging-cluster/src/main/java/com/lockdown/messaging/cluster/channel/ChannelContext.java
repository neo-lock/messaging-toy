package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public interface ChannelContext extends ChannelInBound {


    ChannelHandler handler();

    ChannelPipeline pipeline();

    ChannelEventLoop eventLoop();


}
