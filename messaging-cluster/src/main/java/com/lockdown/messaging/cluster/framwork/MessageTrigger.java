package com.lockdown.messaging.cluster.framwork;

public interface MessageTrigger<T extends ChannelSlot, M,A extends MessageAcceptor> {

    void messageTriggered(T channelSlot, M message);

    void registerAcceptor(A acceptor);
}
