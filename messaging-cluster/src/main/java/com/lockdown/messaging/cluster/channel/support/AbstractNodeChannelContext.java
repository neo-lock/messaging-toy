package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.NodeChannelContext;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

@SuppressWarnings("unchecked")
public abstract class AbstractNodeChannelContext extends AbstractChannelContext<AbstractNodeChannelContext> implements NodeChannelContext {


    AbstractNodeChannelContext(ChannelEventLoop eventLoop) {
        super(eventLoop);
    }


    @Override
    public void fireChannelReceived(Object message) {
        eventLoop().execute(() -> findNext().invokeChannelReceived(message));

    }

    @Override
    public void fireChannelClosed() {
        eventLoop().execute(() -> findNext().invokeChannelClosed());
    }

    @Override
    public void fireExceptionCaught(Throwable throwable) {
        eventLoop().execute(() -> findNext().invokeChannelExceptionCaught(throwable));
    }


    protected void invokeChannelExceptionCaught(Throwable throwable) {
        (this.handler()).exceptionCaught(this, throwable);
    }


    protected void invokeChannelClosed() {
        (this.handler()).channelClosed(this);
    }

    @Override
    protected AbstractNodeChannelContext findNext() {
        return this.next;
    }
}
