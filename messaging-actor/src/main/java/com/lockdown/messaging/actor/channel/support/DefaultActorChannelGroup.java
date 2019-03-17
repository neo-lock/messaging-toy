package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelGroup;
import com.lockdown.messaging.actor.channel.ActorChannelInitializer;
import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultActorChannelGroup implements ActorChannelGroup{

    private Map<Destination,ActorChannel> actorChannelMap = new ConcurrentHashMap<>();
    private final ChannelEventLoop eventLoop;
    private final ActorChannelInitializer channelInitializer;

    public DefaultActorChannelGroup(ChannelEventLoop eventLoop) {
        this.eventLoop = eventLoop;
        this.channelInitializer = new ActorChannelInitializer();
    }

    @Override
    public void addActorChannel(ActorChannel actorChannel) {
        actorChannelMap.put(actorChannel.destination(),actorChannel);
    }

    @Override
    public ActorChannel getChannel(Destination destination) {
        return actorChannelMap.get(destination);
    }

    @Override
    public ActorChannel newInstance(ChannelFuture future, ServerDestination destination) {
        ActorDestination actorDestination = new ActorDestination(future.channel().id().asLongText(),destination);
        ActorChannel actorChannel = new ActorChannel(eventLoop,actorDestination,future);
        this.channelInitializer.initialize(actorChannel);
        addActorChannel(actorChannel);
        return actorChannel;
    }
}
