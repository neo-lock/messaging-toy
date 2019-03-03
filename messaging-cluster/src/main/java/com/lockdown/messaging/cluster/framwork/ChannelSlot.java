package com.lockdown.messaging.cluster.framwork;

import io.netty.channel.ChannelId;

public interface ChannelSlot<D, M> extends Findable<D>, MessageWritable<M> {

    void close();

    void receivedMessageEvent(M message);

    void inactiveEvent();

    void exceptionCaughtEvent(Throwable cause);

    ChannelId channelId();

    Enum<?> slotType();

}
