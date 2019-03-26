package com.lockdown.messaging.actor.sockethandler;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import com.lockdown.messaging.cluster.sockethandler.AbstractCommandHandler;
import io.netty.channel.ChannelHandlerContext;

public class ActorSocketHandler extends AbstractCommandHandler {


    private final ActorChannelEventLoop eventLoop;
    private ActorDestination destination;

    public ActorSocketHandler(ActorServerContext serverContext) {
        super(serverContext);
        this.eventLoop = (ActorChannelEventLoop) serverContext.eventLoop();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (isLocalPort(ctx)) {
            return;
        }
        destination = eventLoop.registerActorChannel(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChannelEvent event = new ChannelEvent(ActorChannel.type(), ChannelEventType.CHANNEL_READ, destination, msg);
        eventLoop.channelEvent(event);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (null != destination) {
            ChannelEvent event = new ChannelEvent(ActorChannel.type(), ChannelEventType.CHANNEL_CLOSE, destination);
            eventLoop.channelEvent(event);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
