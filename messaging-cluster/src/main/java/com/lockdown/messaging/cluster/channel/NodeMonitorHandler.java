package com.lockdown.messaging.cluster.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitorHandler implements ChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(MessagingChannelContext ctx, Object message) {
        logger.info(" read message {}",message);
        throw new RuntimeException("test");
        //ctx.fireChannelReceived(message);
    }

    @Override
    public void channelRegistered(MessagingChannelContext ctx) {

    }

    @Override
    public void channelClosed(MessagingChannelContext ctx) {

    }

    @Override
    public void exceptionCaught(MessagingChannelContext ctx, Throwable throwable) {

    }
}
