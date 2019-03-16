package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandler;
import com.lockdown.messaging.cluster.channel.ChannelOutboundHandler;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;


public abstract class AbstractChannelContext implements ChannelContext {

    private final ChannelPipeline pipeline;
    private final boolean inbound;
    private final boolean outbound;
    volatile AbstractChannelContext next;
    volatile AbstractChannelContext prev;


    AbstractChannelContext(ChannelPipeline pipeline, boolean inbound, boolean outbound) {
        this.pipeline = pipeline;
        this.inbound = inbound;
        this.outbound = outbound;
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public ChannelEventLoop eventLoop() {
        return pipeline.channel().eventLoop();
    }

    @Override
    public void fireExceptionCaught(Throwable throwable) {
        invokeExceptionCaught(findNex(), throwable);
    }

    private void invokeExceptionCaught(final AbstractChannelContext next, Throwable throwable) {
        next.invokeExceptionCaught(throwable);
    }

    private void invokeExceptionCaught(Throwable throwable) {
        (this.handler()).exceptionCaught(this, throwable);
    }

    @Override
    public void fireChannelClosed() {
        invokeChannelClosed(findNex());
    }

    private void invokeChannelClosed(final AbstractChannelContext next) {
        next.invokeChannelClosed();
    }

    private void invokeChannelClosed() {
        (this.handler()).channelClosed(this);
    }


    @Override
    public void fireChannelReceived(Object message) {
        invokeChannelReceived(findNextInbound(), message);
    }

    private void invokeChannelReceived(final AbstractChannelContext next, Object message) {
        next.invokeChannelReceived(message);
    }

    private void invokeChannelReceived(Object message) {
        ((ChannelInboundHandler) this.handler()).channelReceived(this, message);
    }

    @Override
    public void fireChannelWrite(Object message) {
        invokeChannelWrite(findPrevOutbound(), message);
    }

    private void invokeChannelWrite(final AbstractChannelContext next, Object message) {
        eventLoop().execute(() -> next.invokeChannelWrite(message));

    }

    private void invokeChannelWrite(Object message) {
        ((ChannelOutboundHandler) this.handler()).channelWrite(this, message);
    }

    private AbstractChannelContext findNex() {
        return this.next;
    }

    private AbstractChannelContext findNextInbound() {
        AbstractChannelContext current = this;
        do {
            current = current.next;
        } while (!current.inbound);
        return current;
    }

    private AbstractChannelContext findPrevOutbound() {
        AbstractChannelContext current = this;
        do {
            current = current.prev;
        } while (!current.outbound);
        return current;
    }

}
