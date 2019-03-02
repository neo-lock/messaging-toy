package com.lockdown.messaging.cluster.framwork;

public interface MessageTrigger<T extends ChannelSlot, M> {


    void messageTriggered(T channelSlot, M message);


//    <P extends MessageAcceptor<T, M>> void registerAcceptor(P acceptor);

    void registerAcceptor(MessageAcceptor<T, M> acceptor);
}
