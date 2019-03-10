package com.lockdown.messaging.cluster.channel;

import java.util.concurrent.ExecutorService;

public class DefaultMessagingChannelPipeline implements MessagingChannelPipeline {

    private final DefaultMessagingChannel channel;
    private AbstractMessagingChannelContext head;
    private AbstractMessagingChannelContext tail;
    private ExecutorService executor;


    DefaultMessagingChannelPipeline(DefaultMessagingChannel channel, ExecutorService executor){
        this.executor = executor;
        this.channel = channel;
        head = new DefaultMessagingChannelContext(executor,new HeadChannelHandler());
        tail = new DefaultMessagingChannelContext(executor,new TailChannelHandler());
        head.next = tail;
        tail.prev = head;
    }





    @Override
    public MessagingChannelPipeline addLast(ChannelHandler handler) {
        DefaultMessagingChannelContext ctx = new DefaultMessagingChannelContext(executor,handler);
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
