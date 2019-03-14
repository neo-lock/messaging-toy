package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.LocalChannelContext;
import com.lockdown.messaging.cluster.channel.LocalChannelHandler;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeMonitoredHandler implements LocalChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(LocalChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        if (event.getParam() instanceof NodeMonitored) {
            LocalNode localNode = ctx.pipeline().channel().localNode();
            NodeMonitored monitored = (NodeMonitored) event.getParam();
            if (localNode.isAttached()) {
                logger.warn(" current node monitored,ignore command !");
                return;
            }
            localNode.attachTo(monitored.getSource());
            logger.info("注册成功！");
            localNode.printNodes();
        } else {
            ctx.fireChannelReceived(message);
        }

    }
}
