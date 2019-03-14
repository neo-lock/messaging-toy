package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.LocalChannelContext;
import com.lockdown.messaging.cluster.channel.LocalChannelHandler;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeClosedHandler implements LocalChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(LocalChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        if (event.getParam() instanceof NodeClosed) {
            logger.info(" 执行 Node Closed !");
            LocalNode localNode = ctx.pipeline().channel().localNode();
            NodeClosed closed = (NodeClosed) event.getParam();
            ctx.eventLoop().nodeChannelGroup().removeNodeChannel(closed.getSource());
            localNode.monitorCompareAndSet(closed.getSource(), null);
            //如果当前节点是父节点,需要重新注册
            if (localNode.attachedCompareAndSet(closed.getSource(), null)) {
                localNode.registerRandomNode();
            }
        } else {
            ctx.fireChannelReceived(message);
        }

    }
}
