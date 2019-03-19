package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeMonitoredHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {

        ChannelEvent event = (ChannelEvent) message;
        if (event.getParam() instanceof NodeMonitored) {
            logger.debug("收到Monitored 信息{}", message);
            LocalNode localNode = ((LocalChannel) ctx.pipeline().channel()).localNode();
            NodeMonitored monitored = (NodeMonitored) event.getParam();
            if (localNode.isAttached()) {
                logger.warn(" current node monitored,ignore command !");
                return;
            }
            localNode.attachTo(monitored.getSource());
            logger.info("注册成功！{}", monitored.getSource());
            localNode.printNodes();
        } else {
            ctx.fireChannelReceived(message);
        }
    }
}
