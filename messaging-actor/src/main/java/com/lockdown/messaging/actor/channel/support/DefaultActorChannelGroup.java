package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelGroup;
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

public class DefaultActorChannelGroup implements ActorChannelGroup {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ChannelEventLoop eventLoop;
    private final ActorChannelInitializer channelInitializer;
    private Map<Destination, ActorChannel> actorChannelMap = new ConcurrentHashMap<>();

    public DefaultActorChannelGroup(ChannelEventLoop eventLoop) {
        this.eventLoop = eventLoop;
        this.channelInitializer = new ActorChannelInitializer();
    }

    @Override
    public void addActorChannel(ActorChannel actorChannel) {
        logger.info("添加ActorChannel {}",actorChannel.destination());
        actorChannelMap.put(actorChannel.destination(), actorChannel);
    }

    @Override
    public ActorChannel getChannel(Destination destination) {
        return actorChannelMap.get(destination);
    }

    @Override
    public Collection<ActorChannel> actorChannels() {
        return  actorChannelMap.values();
    }

    @Override
    public void printNodes() {
        logger.info(" actor nodes {}",actorChannelMap.keySet());
    }

    @Override
    public void removeActorChannel(Destination destination) {
        actorChannelMap.remove(destination);
    }

    @Override
    public ActorChannel newInstance(ChannelFuture future, ServerDestination destination) {
        ActorDestination actorDestination = new ActorDestination(future.channel().id().asLongText(), destination);
        ActorChannel actorChannel = new ActorChannel(eventLoop, actorDestination, future);
        this.channelInitializer.initialize(actorChannel);
        addActorChannel(actorChannel);
        return actorChannel;
    }
}
