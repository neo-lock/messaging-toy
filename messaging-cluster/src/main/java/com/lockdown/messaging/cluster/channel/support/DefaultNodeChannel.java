package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.channel.NodeChannelPipeline;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultNodeChannel implements NodeChannel {


    private final ChannelFuture channelFuture;
    private final ServerDestination destination;
    private final DefaultNodeChannelPipeline pipeline;
    private final ChannelEventLoop eventLoop;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    DefaultNodeChannel(ChannelEventLoop eventLoop, ChannelFuture channel, ServerDestination destination) {
        this.eventLoop = eventLoop;
        this.channelFuture = channel;
        this.destination = destination;
        this.pipeline = new DefaultNodeChannelPipeline(this);
    }


    @Override
    public NodeChannelPipeline pipeline() {
        return pipeline;
    }


    @Override
    public void writeAndFlush(Object obj) {
        channelFuture.channel().writeAndFlush(obj);
    }


    @Override
    public void close() {
        channelFuture.channel().close();
    }

    @Override
    public ServerDestination destination() {
        return destination;
    }

    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        switch (channelEvent.getEventType()){
            case CHANNEL_WRITE:{
                writeAndFlush(channelEvent.getParam());
                break;
            }
            case CHANNEL_CLOSE:{
                pipeline.fireChannelClosed();
                break;
            }
            default:{
                pipeline.fireChannelReceived(channelEvent);
            }
        }
    }

    @Override
    public ChannelEventLoop eventLoop() {
        return eventLoop;
    }

}
