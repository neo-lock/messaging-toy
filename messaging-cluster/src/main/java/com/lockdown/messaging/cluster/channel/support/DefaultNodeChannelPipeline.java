package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.channel.NodeChannelContext;
import com.lockdown.messaging.cluster.channel.NodeChannelHandler;
import com.lockdown.messaging.cluster.channel.NodeChannelPipeline;

public class DefaultNodeChannelPipeline implements NodeChannelPipeline {

    private final NodeChannel channel;
    private AbstractNodeChannelContext head;
    private AbstractNodeChannelContext tail;


    DefaultNodeChannelPipeline(NodeChannel channel) {
        this.channel = channel;
        head = new DefaultNodeChannelContext(channel.eventLoop(), new HeadNodeChannelHandler(), this);
        tail = new DefaultNodeChannelContext(channel.eventLoop(), new TailNodeChannelHandler(), this);
        head.next = tail;
        tail.prev = head;
    }


    @Override
    public NodeChannelPipeline addLast(NodeChannelHandler handler) {
        DefaultNodeChannelContext ctx = new DefaultNodeChannelContext(channel.eventLoop(), handler, this);
        AbstractNodeChannelContext prev = this.tail.prev;
        prev.next = ctx;
        ctx.next = tail;
        tail.prev = ctx;
        return this;
    }

    @Override
    public NodeChannel channel() {
        return channel;
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
        head.fireExceptionCaught(throwable);
    }

    private class HeadNodeChannelHandler implements NodeChannelHandler {

        @Override
        public void channelReceived(NodeChannelContext ctx, Object message) {
            ctx.fireChannelReceived(message);
        }

        @Override
        public void channelClosed(NodeChannelContext ctx) {
            ctx.fireChannelClosed();
        }

        @Override
        public void exceptionCaught(NodeChannelContext ctx, Throwable throwable) {
            ctx.fireExceptionCaught(throwable);
        }
    }

    private class TailNodeChannelHandler implements NodeChannelHandler {

        @Override
        public void channelReceived(NodeChannelContext ctx, Object message) {

        }

        @Override
        public void channelClosed(NodeChannelContext ctx) {

        }

        @Override
        public void exceptionCaught(NodeChannelContext ctx, Throwable throwable) {

        }
    }
}
