package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.MessagingChannelContext;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public abstract class AbstractMessagingChannelContext implements MessagingChannelContext {


    private final ChannelEventLoop eventLoop;
    volatile AbstractMessagingChannelContext next;
    volatile AbstractMessagingChannelContext prev;


    AbstractMessagingChannelContext(ChannelEventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }


    @Override
    public void fireChannelRegistered() {
        eventLoop.execute(() -> findNext().invokeChannelRegistered());

    }

    @Override
    public void fireChannelReceived(Object message) {
        eventLoop.execute(() -> findNext().invokeChannelReceived(message));

    }

    @Override
    public void fireChannelClosed() {
        eventLoop.execute(() -> findNext().invokeChannelClosed());
    }

    @Override
    public void fireExceptionCaught(Throwable throwable) {
        eventLoop.execute(() -> findNext().invokeChannelExceptionCaught(throwable));
    }

    private void invokeChannelExceptionCaught(Throwable throwable) {
        (this.handler()).exceptionCaught(this, throwable);
    }

    private void invokeChannelClosed() {
        (this.handler()).channelClosed(this);
    }

    private void invokeChannelRegistered() {
        try {
            (this.handler()).channelRegistered(this);
        } catch (Throwable throwable) {
            fireExceptionCaught(throwable);
        }
    }

    private void invokeChannelReceived(Object message) {
        try {
            (this.handler()).channelReceived(this, message);
        } catch (Throwable t) {
            fireExceptionCaught(t);
        }
    }

    private AbstractMessagingChannelContext findNext() {
        return this.next;
    }
}
