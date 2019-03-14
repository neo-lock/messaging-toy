package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.LocalChannel;
import com.lockdown.messaging.cluster.channel.LocalChannelContext;
import com.lockdown.messaging.cluster.channel.LocalChannelHandler;
import com.lockdown.messaging.cluster.channel.LocalChannelPipeline;

public class DefaultLocalChannelPipeline implements LocalChannelPipeline {


    private final LocalChannel channel;
    private AbstractChannelContext head;
    private AbstractChannelContext tail;


    DefaultLocalChannelPipeline(LocalChannel channel) {
        this.channel = channel;
        head = new DefaultLocalChannelContext(channel.eventLoop(), new HeadChannelHandler(), this);
        tail = new DefaultLocalChannelContext(channel.eventLoop(), new TailChannelHandler(), this);
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public LocalChannelPipeline addLast(LocalChannelHandler handler) {
        DefaultLocalChannelContext ctx = new DefaultLocalChannelContext(channel.eventLoop(), handler, this);
        AbstractChannelContext prev = this.tail.prev;
        prev.next = ctx;
        ctx.next = tail;
        tail.prev = ctx;
        return this;
    }

    @Override
    public LocalChannel channel() {
        return channel;
    }

    @Override
    public void fireChannelReceived(Object message) {
        head.fireChannelReceived(message);
    }


    private class HeadChannelHandler implements LocalChannelHandler {


        @Override
        public void channelReceived(LocalChannelContext ctx, Object message) {

        }
    }

    private class TailChannelHandler implements LocalChannelHandler {

        @Override
        public void channelReceived(LocalChannelContext ctx, Object message) {

        }
    }


}
