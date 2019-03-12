package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelHandler;
import com.lockdown.messaging.cluster.channel.MessagingChannelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TailChannelHandler implements ChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(MessagingChannelContext ctx, Object message) {
        logger.info(" tail discard {}", message);
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
