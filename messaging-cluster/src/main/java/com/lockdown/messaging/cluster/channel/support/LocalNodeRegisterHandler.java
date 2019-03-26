package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeRegisterHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        if (event.getParam() instanceof NodeRegister) {
            NodeRegister command = (NodeRegister) event.getParam();
            LocalNode localNode = ((LocalChannel) ctx.pipeline().channel()).localNode();
            logger.debug("开始监控{}", command.getSource());
            localNode.monitor(command.getSource());
            localNode.printNodes();

            writeMonitor(ctx, command);
            notifyChannel(ctx, command);
        } else {
            ctx.fireChannelReceived(message);
        }
    }


    private void writeMonitor(ChannelContext ctx, NodeRegister register) {
        LocalNode localNode = ((LocalChannel) ctx.pipeline().channel()).localNode();
        NodeMonitored monitored = new NodeMonitored(localNode.destination());
        logger.info("发送监控响应 {} ", monitored);
        ChannelEvent monitorEvent = new ChannelEvent(ctx.pipeline().channel(), NodeChannel.type(), ChannelEventType.CHANNEL_WRITE, register.getSource(), monitored);
        ctx.eventLoop().channelEvent(monitorEvent);
    }

    private void notifyChannel(ChannelContext ctx, NodeRegister register) {
        LocalNode localNode = ((LocalChannel) ctx.pipeline().channel()).localNode();
        NodeRegisterForward registerForward = new NodeRegisterForward(localNode.destination(), register.getSource());
        ctx.eventLoop().notifyWriteMessage(NodeChannel.type(), registerForward, register.getSource());
    }


}
