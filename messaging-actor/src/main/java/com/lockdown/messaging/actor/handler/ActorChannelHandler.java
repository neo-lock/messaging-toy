package com.lockdown.messaging.actor.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ActorChannelHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(" 活动==============!");
//        actor = applicationContext.getActorFactory().newActorInstance(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        actor.EventTriggered(evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(" 收到消息 {}",msg);
//        actor.acceptMessage(msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        actor.inactive();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        actor.exceptionCaught(cause);
    }

}
