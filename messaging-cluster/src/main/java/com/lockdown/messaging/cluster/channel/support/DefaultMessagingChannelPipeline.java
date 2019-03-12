package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelHandler;
import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.channel.MessagingChannelPipeline;

public class DefaultMessagingChannelPipeline implements MessagingChannelPipeline {

    private final DefaultRemoteNodeChannel channel;
    private AbstractMessagingChannelContext head;
    private AbstractMessagingChannelContext tail;


    DefaultMessagingChannelPipeline(DefaultRemoteNodeChannel channel) {
        this.channel = channel;
        head = new DefaultMessagingChannelContext(channel.eventLoop(), new HeadChannelHandler());
        tail = new DefaultMessagingChannelContext(channel.eventLoop(), new TailChannelHandler());
        head.next = tail;
        tail.prev = head;
    }


    @Override
    public MessagingChannelPipeline addLast(ChannelHandler handler) {
        DefaultMessagingChannelContext ctx = new DefaultMessagingChannelContext(channel.eventLoop(), handler);
        AbstractMessagingChannelContext prev = this.tail.prev;
        prev.next = ctx;
        ctx.next = tail;
        tail.prev = ctx;
        return this;
    }

    @Override
    public MessagingChannel channel() {
        return channel;
    }


    @Override
    public void fireChannelRegistered() {
        head.fireChannelRegistered();
    }


    @Override
    public void fireChannelReceived(Object message) {
        head.fireChannelReceived(message);
    }


    @Override
    public void fireChannelClosed() {
        head.fireChannelClosed();
    }


    @Override
    public void fireExceptionCaught(Throwable throwable) {

    }
}
