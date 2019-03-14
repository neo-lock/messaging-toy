package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.NodeChannelHandler;
import com.lockdown.messaging.cluster.channel.NodeChannelPipeline;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public class DefaultNodeChannelContext extends AbstractNodeChannelContext {

    private final NodeChannelHandler nodeChannelHandler;
    private final NodeChannelPipeline pipeline;

    DefaultNodeChannelContext(ChannelEventLoop eventLoop, NodeChannelHandler nodeChannelHandler, NodeChannelPipeline pipeline) {
        super(eventLoop);
        this.nodeChannelHandler = nodeChannelHandler;
        this.pipeline = pipeline;
    }


    @Override
    public NodeChannelHandler handler() {
        return nodeChannelHandler;
    }

    @Override
    public NodeChannelPipeline pipeline() {
        return pipeline;
    }
}
