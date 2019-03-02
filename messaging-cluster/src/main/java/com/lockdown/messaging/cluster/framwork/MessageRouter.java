package com.lockdown.messaging.cluster.framwork;

public interface MessageRouter<T extends ChannelSlot<D, M>, D extends Destination, M> extends MessageEmitter<D, M>, MessageAcceptor<T, M> {



}
