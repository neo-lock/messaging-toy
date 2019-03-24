package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.*;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorGroup;
import com.lockdown.messaging.actor.channel.ActorChannelInitializer;
import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultActorGroup implements ActorGroup {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ChannelEventLoop eventLoop;
    private final ActorChannelInitializer channelInitializer;
    private final ActorServerContext actorServerContext;
    private Map<Destination, ActorChannel> actorChannelMap = new ConcurrentHashMap<>();
    private Map<Destination,Actor> actorMap = new ConcurrentHashMap<>();


    DefaultActorGroup(ChannelEventLoop eventLoop, ActorServerContext actorServerContext) {
        this.eventLoop = eventLoop;
        this.actorServerContext = actorServerContext;
        this.channelInitializer = new ActorChannelInitializer();
    }


    @Override
    public void addActor(Actor actor) {
        actorMap.put(actor.destination(),actor);
    }


    @Override
    public Actor getActor(Destination destination) {
        return actorMap.get(destination);
    }


    @Override
    public Collection<Actor> allActors() {
        return actorMap.values();
    }

    @Override
    public void printNodes() {
        logger.debug(" actor nodes {}",actorChannelMap.keySet());
    }

    @Override
    public void removeActor(Destination destination) {
        logger.info(" destination {}",destination);
        actorMap.remove(destination);
    }

    @Override
    public ActorChannel newInstance(ChannelFuture future, ServerDestination destination) {
        ActorDestination actorDestination = new ActorDestination(future.channel().id().asLongText(), destination);
        AbstractActor actor = actorServerContext.actorFactory().newInstance();
        ActorChannel actorChannel = new ActorChannel(eventLoop, actorDestination, future,actor);
        this.channelInitializer.initialize(actorChannel);
        actor.setActorChannel(actorChannel);
        addActor(actor);
        return actorChannel;
    }
}
