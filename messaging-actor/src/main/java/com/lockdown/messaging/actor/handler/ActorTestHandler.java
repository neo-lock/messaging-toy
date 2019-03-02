package com.lockdown.messaging.actor.handler;

import com.lockdown.messaging.actor.ActorSlot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorTestHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ActorBeanFactory factory;
    private ActorSlot actorSlot;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(" actorSlot handler active !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        actorSlot = factory.newInstance(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(" 收到消息==========================      {} ", msg);
        actorSlot.receivedMessage(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}
