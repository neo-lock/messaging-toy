package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultChannelPipeline implements ChannelPipeline {


    private final Channel channel;
    private AbstractChannelContext head;
    private AbstractChannelContext tail;

    DefaultChannelPipeline(Channel channel) {
        this.channel = channel;
        this.head = new DefaultChannelContext(this, new HeadChannelHandler(channel));
        this.tail = new DefaultChannelContext(this, new TailChannelHandler());
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

    private class HeadChannelHandler extends ChannelInboundHandlerAdapter implements ChannelOutboundHandler {

        private final Channel channel;

        private HeadChannelHandler(Channel channel) {
            this.channel = channel;
        }

        @Override
        public void channelWrite(ChannelContext ctx, Object message) {
            channel.writeAndFlush(message);
        }
    }

    private class TailChannelHandler implements ChannelOutboundHandler, ChannelInboundHandler {

        private Logger logger = LoggerFactory.getLogger(TailChannelHandler.class);

        @Override
        public void channelWrite(ChannelContext ctx, Object message) {
            logger.debug("Tail channel write message {}",message);
            ctx.fireChannelWrite(message);
        }

        @Override
        public void channelReceived(ChannelContext ctx, Object message) {
            logger.warn("channel received discard !{}",message);
        }

        @Override
        public void exceptionCaught(ChannelContext ctx, Throwable throwable) {
            logger.warn("exception caught discard !");
        }

        @Override
        public void channelClosed(ChannelContext ctx) {
            logger.warn("channel closed discard!");
        }
    }

}
