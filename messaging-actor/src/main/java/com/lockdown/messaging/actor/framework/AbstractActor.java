package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorNodeType;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Objects;

public abstract class AbstractActor implements Actor {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ActorDestination destination;
    private ActorMonitor actorMonitor;
    private ChannelFuture channelFuture;
    private ActorMessageEmitter messageEmitter;

    public AbstractActor() {
    }

    protected ActorDestination getDestination() {
        return destination;
    }

    void setDestination(ActorDestination destination) {
        if (Objects.isNull(destination)) {
            throw new IllegalArgumentException(" destination can't be null !");
        }
        if (Objects.nonNull(this.destination)) {
            throw new IllegalStateException(" destination already set !");
        }
        logger.info("当前actor destination {}",destination);
        this.destination = destination;
    }

    void setActorMonitor(ActorMonitor actorMonitor) {
        if (Objects.isNull(actorMonitor)) {
            throw new IllegalArgumentException(" actor monitor can't be null !");
        }
        if (Objects.nonNull(this.actorMonitor)) {
            throw new IllegalStateException(" actor monitor already set !");
        }
        this.actorMonitor = actorMonitor;
    }

    void setChannelFuture(ChannelFuture channelFuture) {
        if (Objects.isNull(channelFuture)) {
            throw new IllegalArgumentException(" channel future can't be null !");
        }
        if (Objects.nonNull(this.channelFuture)) {
            throw new IllegalStateException(" channel future already set !");
        }
        InetSocketAddress address = (InetSocketAddress) channelFuture.channel().localAddress();
        logger.info("当前actor address {}",address);

        this.channelFuture = channelFuture;
    }

    void setMessageEmitter(ActorMessageEmitter messageEmitter) {
        if (Objects.isNull(messageEmitter)) {
            throw new IllegalArgumentException(" message emitter can't be null !");
        }
        if (Objects.nonNull(this.messageEmitter)) {
            throw new IllegalStateException(" message emitter already set !");
        }
        this.messageEmitter = messageEmitter;
    }

    @Override
    public  void close() {
        channelFuture.channel().close();
        actorClosed();
    }


    protected abstract void actorClosed();

    protected ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    @Override
    public  void writeMessage(Object message) {
        channelFuture.channel().writeAndFlush(message);
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
        messageEmitter.sendMessage(this,destination, message);
    }

    @Override
    public Enum<?> slotType() {
        return ActorNodeType.ACTOR_NODE;
    }
}
