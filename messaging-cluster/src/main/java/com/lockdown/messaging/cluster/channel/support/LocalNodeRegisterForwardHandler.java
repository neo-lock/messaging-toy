package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.command.NodeGreeting;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeRegisterForwardHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {

        ChannelEvent event = (ChannelEvent) message;
        if (event.getParam() instanceof NodeRegisterForward) {
            logger.debug("收到RegisterForward{}", message);
            LocalNode localNode = ((LocalChannel) ctx.pipeline().channel()).localNode();
            NodeRegisterForward registerForward = (NodeRegisterForward) event.getParam();
            if (localNode.isAttached() && !localNode.attachedCompareAndSet(registerForward.getSource(), registerForward.getTarget())) {
                ctx.eventLoop().nodeChannelGroup().connectOnNotExists(registerForward.getTarget());
                ChannelEvent greeting = new ChannelEvent(ctx.pipeline().channel(), NodeChannel.type(), ChannelEventType.CHANNEL_WRITE, registerForward.getTarget(), new NodeGreeting(localNode.destination()));
                ctx.eventLoop().channelEvent(greeting);
            } else {
                localNode.registerToCluster(registerForward.getTarget());
            }
        } else {
            ctx.fireChannelReceived(message);
        }
    }


}
