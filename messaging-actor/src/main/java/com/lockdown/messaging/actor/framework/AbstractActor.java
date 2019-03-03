package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorNodeType;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class AbstractActor implements Actor {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private ActorDestination destination;
    private ActorMonitor actorMonitor;
    private ChannelFuture channelFuture;
    private ActorMessageEmitter messageEmitter;

    public AbstractActor() {
    }

    public void setDestination(ActorDestination destination) {
        if (Objects.isNull(destination)) {
            throw new IllegalArgumentException(" destination can't be null !");
        }
        if (Objects.nonNull(this.destination)) {
            throw new IllegalStateException(" destination already set !");
        }
        this.destination = destination;
    }

    public void setActorMonitor(ActorMonitor actorMonitor) {
        if (Objects.isNull(actorMonitor)) {
            throw new IllegalArgumentException(" actor monitor can't be null !");
        }
        if (Objects.nonNull(this.actorMonitor)) {
            throw new IllegalStateException(" actor monitor already set !");
        }
        this.actorMonitor = actorMonitor;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        if (Objects.isNull(channelFuture)) {
            throw new IllegalArgumentException(" channel future can't be null !");
        }
        if (Objects.nonNull(this.channelFuture)) {
            throw new IllegalStateException(" channel future already set !");
        }
        this.channelFuture = channelFuture;
    }

    public void setMessageEmitter(ActorMessageEmitter messageEmitter) {
        if (Objects.isNull(messageEmitter)) {
            throw new IllegalArgumentException(" message emitter can't be null !");
        }
        if (Objects.nonNull(this.messageEmitter)) {
            throw new IllegalStateException(" message emitter already set !");
        }
        this.messageEmitter = messageEmitter;
    }

    @Override
    public final void close() {
        channelFuture.channel().close();
    }

    @Override
    public final void receivedMessageEvent(Object message) {
        actorMonitor.acceptedMessage(this, message);
    }


    @Override
    public final void inactiveEvent() {
        actorMonitor.inactive(this);
    }

    @Override
    public final void exceptionCaughtEvent(Throwable cause) {
        actorMonitor.exceptionCaught(this, cause);
    }


    @Override
    public final ChannelId channelId() {
        return channelFuture.channel().id();
    }

    @Override
    public final ActorDestination destination() {
        return destination;
    }


    @Override
    public final void sendMessage(ActorDestination destination, Object message) {
        messageEmitter.sendMessage(destination, message);
    }

    @Override
    public Enum<?> slotType() {
        return ActorNodeType.ACTOR_NODE;
    }
}
