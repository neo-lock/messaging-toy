package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.channel.support.LocalChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;

public class LocalChannelEventInvoker implements ChannelTypeEventInvoker {
    private final LocalChannel localChannel;


    public LocalChannelEventInvoker(LocalChannel localChannel) {
        this.localChannel = localChannel;
    }

    @Override
    public void handleEvent(ChannelEvent event) {
        localChannel.handleEvent(event);
    }

    @Override
    public Enum<?> supported() {
        return LocalChannel.type();
    }
}
