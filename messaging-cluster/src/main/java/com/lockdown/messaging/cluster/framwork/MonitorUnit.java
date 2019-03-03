package com.lockdown.messaging.cluster.framwork;

public interface Slot<T,M> {


    void receivedMessage(T slot, M message);


    void inactive(T slot);


    void exceptionCaught(T slot, Throwable cause);


}
