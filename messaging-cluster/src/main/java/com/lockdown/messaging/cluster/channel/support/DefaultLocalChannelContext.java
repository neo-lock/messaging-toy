package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.LocalChannelContext;
import com.lockdown.messaging.cluster.channel.LocalChannelHandler;
import com.lockdown.messaging.cluster.channel.LocalChannelPipeline;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public class DefaultLocalChannelContext extends AbstractChannelContext<AbstractChannelContext> implements LocalChannelContext {


    private final LocalChannelHandler localChannelHandler;
    private final LocalChannelPipeline pipeline;


    DefaultLocalChannelContext(ChannelEventLoop eventLoop, LocalChannelHandler localChannelHandler, LocalChannelPipeline pipeline) {
        super(eventLoop);
        this.localChannelHandler = localChannelHandler;
        this.pipeline = pipeline;
    }

    @Override
    public LocalChannelHandler handler() {
        return localChannelHandler;
    }

    @Override
    public LocalChannelPipeline pipeline() {
        return pipeline;
    }


}
