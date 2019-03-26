package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.EventSource;


public interface Channel extends EventSource {

    void writeAndFlush(Object message);

    void handleEvent(ChannelEvent channelEvent);

    ChannelEventLoop eventLoop();

    ChannelPipeline pipeline();

    void close();


}
