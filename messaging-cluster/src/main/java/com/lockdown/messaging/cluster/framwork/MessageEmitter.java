package com.lockdown.messaging.cluster.framwork;

public interface MessageEmitter<D extends Destination, M> {

    void sendMessage(D destination, M message);

}
