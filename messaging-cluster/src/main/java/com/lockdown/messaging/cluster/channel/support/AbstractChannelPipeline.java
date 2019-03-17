package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.ChannelHandler;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;

public class AbstractChannelPipeline implements ChannelPipeline {
    private final Channel channel;
    private AbstractChannelContext head;
    private AbstractChannelContext tail;

    public AbstractChannelPipeline(Channel channel, ChannelHandler headHandler, ChannelHandler tailHandler) {
        this.channel = channel;
        this.head = new DefaultChannelContext(this, headHandler);
        this.tail = new DefaultChannelContext(this, tailHandler);
        this.head.next = tail;
        this.tail.prev = this.head;
    }

    @Override
    public ChannelPipeline addLast(ChannelHandler handler) {
        DefaultChannelContext context = new DefaultChannelContext(this, handler);
        final AbstractChannelContext prev = this.tail.prev;
        this.tail.prev = context;
        context.next = this.tail;
        context.prev = prev;
        prev.next = context;
        return this;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public void writeAndFlush(Object message) {
        tail.fireChannelWrite(message);
    }

    @Override
    public void fireChannelReceived(Object message) {
        head.fireChannelReceived(message);
    }

    @Override
    public void fireChannelWrite(Object message) {
        tail.fireChannelWrite(message);
    }

    @Override
    public void fireExceptionCaught(Throwable throwable) {
        head.fireExceptionCaught(throwable);
    }

    @Override
    public void fireChannelClosed() {
        head.fireChannelClosed();
    }
}
