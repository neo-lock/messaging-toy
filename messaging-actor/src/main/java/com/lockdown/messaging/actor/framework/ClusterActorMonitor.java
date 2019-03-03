package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.framwork.AbstractChannelSlotMonitor;
import io.netty.channel.ChannelFuture;

public class ClusterActorMonitor extends AbstractChannelSlotMonitor<Actor, ActorDestination, Object>
        implements ActorMonitor, ActorBeanFactory {


    private final ActorMonitoringBeanFactory beanFactory;
    private ActorMessageAcceptor messageAcceptor;

    public ClusterActorMonitor(ActorMonitoringBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beanFactory.setMonitorUnit(this);
    }

    @Override
    public void messageTriggered(Actor actor, Object message) {
        this.messageAcceptor.acceptedMessage(actor, message);
    }


    @Override
    public void registerAcceptor(ActorMessageAcceptor acceptor) {
        this.messageAcceptor = acceptor;
    }


    @Override
    public Actor getInstance(ChannelFuture channelFuture, ActorDestination destination) {
        Actor actor = beanFactory.getInstance(channelFuture, destination);
        destinationContext.putIfAbsent(actor.destination(), actor);
        return actor;
    }

    @Override
    public void acceptedMessage(Actor actor, Object message) {
        messageTriggered(actor, message);
    }
}
