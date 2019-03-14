package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;


public interface Channel {

    ChannelPipeline pipeline();

    void handleEvent(ChannelEvent channelEvent);

    ChannelEventLoop eventLoop();

    ServerDestination destination();

}
