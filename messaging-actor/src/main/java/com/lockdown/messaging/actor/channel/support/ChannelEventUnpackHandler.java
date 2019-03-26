package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.support.ChannelInboundHandlerAdapter;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;

public class ChannelEventUnpackHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        if (message instanceof ChannelEvent) {
            ChannelEvent event = (ChannelEvent) message;
            ctx.fireChannelReceived(event.getParam());
        } else {
            ctx.fireChannelReceived(message);
        }

    }
}
