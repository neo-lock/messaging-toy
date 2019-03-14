package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public abstract class AbstractChannelContext<T extends AbstractChannelContext> implements ChannelContext {

    private final ChannelEventLoop eventLoop;
    volatile T next;
    volatile T prev;
    private Logger logger = LoggerFactory.getLogger(getClass());

    AbstractChannelContext(ChannelEventLoop eventLoop) {
        this.eventLoop = eventLoop;
    }


    @Override
    public ChannelEventLoop eventLoop() {
        return eventLoop;
    }

    @Override
    public void fireChannelReceived(Object message) {
        invokeChannelReceived(findNext(), message);
    }

    private void invokeChannelReceived(final T next, Object message) {
        ChannelEventLoop eventLoop = next.eventLoop();
        eventLoop.execute(() -> next.invokeChannelReceived(message));

    }

    void invokeChannelReceived(Object message) {
        (this.handler()).channelReceived(this, message);
    }


    protected T findNext() {
        return this.next;
    }

}
