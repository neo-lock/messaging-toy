package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorMessageCodec;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.actor.channel.ActorGroup;
import com.lockdown.messaging.cluster.channel.ChannelEventLoopInitializer;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.DisruptorChannelEventLoop;
import io.netty.channel.ChannelHandlerContext;

public class ActorDisruptorChannelEventLoop extends DisruptorChannelEventLoop implements ActorChannelEventLoop {

    private ActorGroup actorGroup;

    public ActorDisruptorChannelEventLoop(ActorServerContext serverContext, ChannelEventLoopInitializer<ChannelEventLoop> eventLoopInitializer) {
        super(serverContext, eventLoopInitializer);
        this.actorGroup = new DefaultActorGroup(this,serverContext);
    }


    @Override
    public ActorDestination registerActorChannel(ChannelHandlerContext ctx) {
        ActorChannel channel = actorGroup.newInstance(ctx.newSucceededFuture(), localDestination());
        return (ActorDestination) channel.destination();
    }

    @Override
    public ActorGroup actorGroup() {
        return actorGroup;
    }


    @Override
    public ActorMessageCodec actorMessageCodec() {
        return ((ActorServerContext)serverContext()).actorMessageCodec();
    }
}
