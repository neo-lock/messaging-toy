package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;


public interface Channel {

    void writeAndFlush(Object message);

    void handleEvent(ChannelEvent channelEvent);

    ChannelEventLoop eventLoop();

    ChannelPipeline pipeline();

    Destination destination();

    void close();


}
