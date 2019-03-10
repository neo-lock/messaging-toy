package com.lockdown.messaging.cluster.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDispatcherHandler implements ChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(MessagingChannelContext ctx, Object message) {
        logger.info(" dispatcher command {}",message);
        ctx.fireChannelReceived(message);
    }

    @Override
    public void channelRegistered(MessagingChannelContext ctx) {

    }

    @Override
    public void channelClosed(MessagingChannelContext ctx) {

    }

    @Override
    public void exceptionCaught(MessagingChannelContext ctx, Throwable throwable) {
        logger.info(" accept exception {}",throwable.getMessage());
    }
}
