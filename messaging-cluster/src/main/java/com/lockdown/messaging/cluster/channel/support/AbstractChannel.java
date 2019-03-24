package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public abstract class AbstractChannel implements Channel {

    private final ChannelEventLoop eventLoop;
    private final Destination destination;
    private ChannelPipeline pipeline;


    AbstractChannel(ChannelEventLoop eventLoop, Destination destination) {
        this.eventLoop = eventLoop;
        this.destination = destination;
        this.pipeline = providerPipeline();
    }


    protected abstract ChannelPipeline providerPipeline();


    @Override
    public ChannelEventLoop eventLoop() {
        return eventLoop;
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public Destination destination() {
        return destination;
    }

}
