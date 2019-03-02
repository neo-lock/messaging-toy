package com.lockdown.messaging.cluster.framwork;

public interface MessageAcceptor<T extends ChannelSlot, M> {

    void acceptedMessage(T channelSlot, M message);

}
