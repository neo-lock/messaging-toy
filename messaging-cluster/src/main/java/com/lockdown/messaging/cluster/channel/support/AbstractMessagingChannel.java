package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public abstract class AbstractMessagingChannel implements MessagingChannel {


    private final ChannelEventLoop eventLoop;

    public AbstractMessagingChannel(ChannelEventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }


    @Override
    public ChannelEventLoop eventLoop() {
        return eventLoop;
    }

    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        //eventLoop.executor().execute(() -> processEvent(channelEvent));
        processEvent(channelEvent);
    }

    protected abstract void processEvent(ChannelEvent event);
}
