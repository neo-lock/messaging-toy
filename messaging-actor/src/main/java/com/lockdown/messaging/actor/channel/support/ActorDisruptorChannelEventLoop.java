package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorMessageCodec;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.actor.channel.ActorChannelGroup;
import com.lockdown.messaging.cluster.channel.ChannelEventLoopInitializer;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.DisruptorChannelEventLoop;
import io.netty.channel.ChannelHandlerContext;

public class ActorDisruptorChannelEventLoop extends DisruptorChannelEventLoop implements ActorChannelEventLoop {

    private ActorChannelGroup actorChannelGroup;
    private ActorMessageCodec actorMessageCodec;


    public ActorDisruptorChannelEventLoop(ActorServerContext serverContext, ChannelEventLoopInitializer<ChannelEventLoop> eventLoopInitializer) {
        super(serverContext, eventLoopInitializer);
        this.actorMessageCodec = serverContext.actorMessageCodec();
        this.actorChannelGroup = new DefaultActorChannelGroup(this);
    }


    @Override
    public ActorDestination registerActorChannel(ChannelHandlerContext ctx) {
        ActorChannel channel = actorChannelGroup.newInstance(ctx.newSucceededFuture(), localDestination());
        return (ActorDestination) channel.destination();
    }

    @Override
    public ActorChannelGroup actorChannelGroup() {
        return actorChannelGroup;
    }

    @Override
    public ActorMessageCodec actorMessageCodec() {
        return actorMessageCodec;
    }
}
