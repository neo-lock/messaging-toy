package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelFactory;
import com.lockdown.messaging.cluster.channel.ChannelInitializer;
import com.lockdown.messaging.cluster.channel.support.LocalChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.ChannelFuture;

public class LocalChannelFactory implements ChannelFactory<LocalChannel, ServerDestination> {

    private final ChannelEventLoop eventLoop;
    private final ChannelInitializer<LocalChannel> channelInitializer;

    public LocalChannelFactory(ChannelEventLoop eventLoop) {
        this.eventLoop = eventLoop;
        this.channelInitializer = new LocalChannelInitializer();
    }

    @Override
    public LocalChannel newInstance(ChannelFuture future, ServerDestination destination) {
        LocalChannel channel = new LocalChannel(eventLoop, destination);
        this.channelInitializer.initialize(channel);
        return channel;
    }
}
