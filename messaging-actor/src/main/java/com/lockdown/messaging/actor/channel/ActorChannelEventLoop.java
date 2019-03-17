package com.lockdown.messaging.actor.channel;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorMessageCodec;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.ChannelHandlerContext;

public interface ActorChannelEventLoop extends ChannelEventLoop {


    ActorDestination registerActorChannel(ChannelHandlerContext ctx);

    ActorChannelGroup actorChannelGroup();

    ActorMessageCodec actorMessageCodec();

}
