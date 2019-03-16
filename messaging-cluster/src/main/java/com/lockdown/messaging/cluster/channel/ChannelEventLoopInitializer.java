package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public interface ChannelEventLoopInitializer<T extends ChannelEventLoop> {


    void initialize(T eventLoop);

}
