package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandler;
import com.lockdown.messaging.cluster.channel.ChannelOutboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TailChannelHandler implements ChannelOutboundHandler, ChannelInboundHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelWrite(ChannelContext ctx, Object message) {
        logger.debug("Tail channel write message {}",message);
        ctx.fireChannelWrite(message);
    }

    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        logger.warn("channel received discard !{}",message);
    }

    @Override
    public void exceptionCaught(ChannelContext ctx, Throwable throwable) {
        logger.warn("exception caught discard !");
    }

    @Override
    public void channelClosed(ChannelContext ctx) {
        logger.warn("channel closed discard!");
    }

}
