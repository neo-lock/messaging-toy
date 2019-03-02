package com.lockdown.messaging.cluster.framwork;

public interface MessageForwardSlot<T,M> {


    void receivedMessage(T slot, M message);


    void inactive(T slot);


    void exceptionCaught(T slot, Throwable cause);

}
