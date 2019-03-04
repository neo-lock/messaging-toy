package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.*;
import io.netty.channel.ChannelFuture;

import java.util.Objects;

public class ClusterActorMonitoringBeanFactory implements ActorMonitoringBeanFactory {


    private ActorMonitor actorMonitor;
    private ClusterActorServerContext<? extends ActorProperties> serverContext;

    public ClusterActorMonitoringBeanFactory(ClusterActorServerContext<? extends ActorProperties> serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public Actor getInstance(ChannelFuture channelFuture, ActorDestination destination) {
        try {
            Class<?> actorClass = Class.forName(serverContext.getProperties().getActorClassName());
            AbstractActor abstractActor = (AbstractActor) actorClass.newInstance();
            abstractActor.setDestination(destination);
            abstractActor.setChannelFuture(channelFuture);
            abstractActor.setActorMonitor(actorMonitor);
            abstractActor.setMessageEmitter(serverContext.actorMessageRouter());
            return abstractActor;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }


    }


    @Override
    public void setMonitorUnit(ActorMonitor monitorUnit) {
        if (Objects.isNull(monitorUnit)) {
            throw new IllegalArgumentException(" monitor can't be null !");
        }
        if (Objects.nonNull(actorMonitor)) {
            throw new IllegalStateException(" monitor already set ");
        }
        this.actorMonitor = monitorUnit;
    }
}
