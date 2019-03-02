package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractActor implements Actor {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private ActorDestination destination;
    private final ActorForwardSlot forwardSlot;
    private final ChannelFuture channelFuture;

    public AbstractActor(ActorDestination destination, ActorForwardSlot forwardSlot, ChannelFuture channelFuture) {
        this.destination = destination;
        this.forwardSlot = forwardSlot;
        this.channelFuture = channelFuture;
    }

    @Override
    public void close() {
        channelFuture.channel().close();
    }

    @Override
    public void receivedMessageEvent(Object message) {
        forwardSlot.receivedMessage(this,message);
    }

    @Override
    public void inactiveEvent() {
        forwardSlot.inactive(this);
    }

    @Override
    public void exceptionCaughtEvent(Throwable cause) {
        forwardSlot.exceptionCaught(this,cause);
    }

    @Override
    public ChannelId channelId() {
        return channelFuture.channel().id();
    }

    @Override
    public ActorDestination destination() {
        return destination;
    }

    @Override
    public void sendMessage(ActorDestination destination, Object message) {

    }

    @Override
    public void writeMessage(Object message) {
        channelFuture.channel().writeAndFlush(message);
    }
}
