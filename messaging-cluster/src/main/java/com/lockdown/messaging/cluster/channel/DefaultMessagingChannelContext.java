package com.lockdown.messaging.cluster.channel;

import java.util.concurrent.ExecutorService;

public class DefaultMessagingChannelContext extends AbstractMessagingChannelContext {

    private final ChannelHandler channelHandler;

    DefaultMessagingChannelContext(ExecutorService executorService, ChannelHandler channelHandler) {
        super(executorService, reactorContext);
        this.channelHandler = channelHandler;
    }


    @Override
    public ChannelHandler handler() {
        return channelHandler;
    }
}
