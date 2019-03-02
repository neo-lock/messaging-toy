package com.lockdown.messaging.actor;

import io.netty.channel.ChannelId;

public interface ActorSlot {

    void sendMessage(Object message);

    void close();

    void receivedMessage(Object object);

    void inactiveEvent();

    void exceptionCaughtEvent(Throwable cause);

    ChannelId channelId();

}
