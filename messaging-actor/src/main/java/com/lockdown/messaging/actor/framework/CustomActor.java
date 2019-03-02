package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import io.netty.channel.ChannelId;

public class CustomActor implements Actor {



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
        return null;
    }

    @Override
    public ActorDestination destination() {
        return null;
    }

    @Override
    public void sendMessage(ActorDestination destination, Object message) {

    }

    @Override
    public void writeMessage(Object message) {

    }
}
