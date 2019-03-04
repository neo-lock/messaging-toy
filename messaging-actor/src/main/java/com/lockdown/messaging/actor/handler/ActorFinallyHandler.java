package com.lockdown.messaging.actor.handler;

import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.Actor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ActorFinallyHandler extends ChannelInboundHandlerAdapter {

    private final ActorServerContext serverContext;
    private Actor actor;

    public ActorFinallyHandler(ActorServerContext serverContext) {
        this.serverContext = serverContext;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        actor = serverContext.actorBeanFactory().getInstance(ctx.newSucceededFuture(), serverContext.createActorDestination(ctx.channel()));
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        actor.receivedMessageEvent(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        actor.exceptionCaughtEvent(cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        actor.inactiveEvent();
    }

}
