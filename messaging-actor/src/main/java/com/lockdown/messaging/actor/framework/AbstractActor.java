package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.MessageForwardSlot;
import com.lockdown.messaging.cluster.node.RemoteNode;
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

    }

    @Override
    public void receivedMessageEvent(Object message) {

    }

    @Override
    public void inactiveEvent() {

    }

    @Override
    public void exceptionCaughtEvent(Throwable cause) {

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
