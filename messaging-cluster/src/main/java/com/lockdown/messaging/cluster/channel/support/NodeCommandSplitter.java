package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandlerAdapter;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeCommandSplitter extends ChannelInboundHandlerAdapter {


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        NodeCommand command = (NodeCommand) event.getParam();
        ChannelEvent local = new ChannelEvent(LocalChannel.type(), ChannelEventType.NODE_READ, command);
        ctx.eventLoop().channelEvent(local);
    }

    @Override
    public void channelClosed(ChannelContext ctx) {
        ChannelEvent event = new ChannelEvent(LocalChannel.type(),ChannelEventType.NODE_READ, ctx.eventLoop().localDestination(), new NodeClosed((ServerDestination) ctx.pipeline().channel().destination()));
        ctx.eventLoop().channelEvent(event);
        ctx.pipeline().channel().close();
    }

}
