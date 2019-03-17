package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandlerAdapter;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeClosedHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {

        ChannelEvent event = (ChannelEvent) message;
        if (event.getParam() instanceof NodeClosed) {
            logger.debug("收到节点关闭消息 {}", message);
            LocalNode localNode = ((LocalChannel) ctx.pipeline().channel()).localNode();
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
