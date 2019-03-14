package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.LocalChannel;
import com.lockdown.messaging.cluster.channel.LocalChannelPipeline;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLocalChannel implements LocalChannel {


    private final DefaultLocalChannelPipeline pipeline;
    private final ChannelEventLoop eventLoop;
    private final LocalNode localNode;
    private final ServerDestination destination;
    private Logger logger = LoggerFactory.getLogger(getClass());

    DefaultLocalChannel(ChannelEventLoop eventLoop, LocalNode localNode) {
        this.eventLoop = eventLoop;
        this.localNode = localNode;
        this.destination = localNode.destination();
        this.pipeline = new DefaultLocalChannelPipeline(this);
    }


    @Override
    public ChannelEventLoop eventLoop() {
        return eventLoop;
    }

    @Override
    public ServerDestination destination() {
        return destination;
    }

    @Override
    public LocalChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public LocalNode localNode() {
        return localNode;
    }

    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        pipeline.fireChannelReceived(channelEvent);
    }

}
