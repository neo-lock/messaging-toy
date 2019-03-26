package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.command.ClusterNature;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有 cluster nature 性质的command 转交给 local channel 处理
 */
public class ClusterNatureForwardHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        if (ClusterNature.class.isAssignableFrom(event.getParam().getClass())) {
            logger.info("Cluster Nature Forward Handler ========  {}", event.getParam());
            NodeCommand command = (NodeCommand) event.getParam();
            ChannelEvent local = new ChannelEvent(ctx.pipeline().channel(), LocalChannel.type(), ChannelEventType.NODE_READ, command);
            ctx.eventLoop().channelEvent(local);
        } else {
            ctx.fireChannelReceived(message);
        }

    }

    @Override
    public void channelClosed(ChannelContext ctx) {
        ChannelEvent event = new ChannelEvent(ctx.pipeline().channel(), LocalChannel.type(), ChannelEventType.NODE_READ, ctx.eventLoop().localDestination(), new NodeClosed((ServerDestination) ctx.pipeline().channel().destination()));
        ctx.eventLoop().channelEvent(event);
        ctx.pipeline().channel().close();
    }

}
