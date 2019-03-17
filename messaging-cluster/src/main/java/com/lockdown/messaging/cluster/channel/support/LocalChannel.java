package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;

import java.util.Objects;

public class LocalChannel extends AbstractChannel {


    private LocalNode localNode;


    public LocalChannel(ChannelEventLoop eventLoop, Destination destination) {
        super(eventLoop, destination);
        Objects.requireNonNull(eventLoop.localNode());
        this.localNode = eventLoop.localNode();
    }

    public static Enum<?> type() {
        return ChannelType.LOCAL;
    }

    @Override
    protected ChannelPipeline providerPipeline() {
        return new DefaultChannelPipeline(this);
    }

    public LocalNode localNode() {
        return localNode;
    }

    @Override
    public void writeAndFlush(Object message) {
        throw new UnsupportedOperationException(" unsupported operation !");
    }

    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        ChannelEventType eventType = channelEvent.getEventType();
        if (eventType == ChannelEventType.NODE_READ | eventType == ChannelEventType.RANDOM_REGISTER | eventType == ChannelEventType.REGISTER_MASTER) {
            this.pipeline().fireChannelReceived(channelEvent);
        } else {
            throw new UnsupportedOperationException("不支持当前事件" + channelEvent.toString());
        }
    }

    @Override
    public void close() {
        //ignore
    }

    private enum ChannelType {
        LOCAL
    }
}
