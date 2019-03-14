package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.NodeChannelContext;
import com.lockdown.messaging.cluster.channel.NodeChannelHandler;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteNodeCommandSplitter implements NodeChannelHandler {


    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void channelReceived(NodeChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        NodeCommand command = (NodeCommand) event.getParam();
        ChannelEvent local = new ChannelEvent(ChannelEventType.NODE_READ,ctx.eventLoop().localDestination(),command);
        ctx.eventLoop().channelEvent(local);
    }

    @Override
    public void channelClosed(NodeChannelContext ctx) {
        ChannelEvent event = new ChannelEvent(ChannelEventType.NODE_READ,ctx.eventLoop().localDestination(),new NodeClosed(ctx.pipeline().channel().destination()));
        ctx.eventLoop().channelEvent(event);
        ctx.pipeline().channel().close();
    }

    @Override
    public void exceptionCaught(NodeChannelContext ctx, Throwable throwable) {

    }
}
