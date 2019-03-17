package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeChannel extends AbstractChannel {

    private final ChannelFuture channelFuture;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public NodeChannel(ChannelEventLoop eventLoop, Destination destination, ChannelFuture channelFuture) {
        super(eventLoop, destination);
        this.channelFuture = channelFuture;
    }

    public static Enum<?> type() {
        return ChannelType.NODE;
    }

    protected ChannelFuture channelFuture() {
        return this.channelFuture;
    }

    @Override
    public void writeAndFlush(Object message) {
        this.channelFuture.channel().writeAndFlush(message);
    }

    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        switch (channelEvent.getEventType()) {
            case CHANNEL_WRITE: {
                pipeline().fireChannelWrite(channelEvent.getParam());
                break;
            }
            case CHANNEL_CLOSE: {
                pipeline().fireChannelClosed();
                break;
            }
            default: {
                pipeline().fireChannelReceived(channelEvent);
            }
        }
    }

    @Override
    public void close() {
        channelFuture.channel().close();
    }

    @Override
    protected ChannelPipeline providerPipeline() {
        return new DefaultChannelPipeline(this);
    }

    private enum ChannelType {
        NODE
    }
}
