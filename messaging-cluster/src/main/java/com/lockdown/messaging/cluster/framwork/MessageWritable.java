package com.lockdown.messaging.cluster.framwork;

public interface MessageWritable<M> {


    void writeMessage(M message);
}
