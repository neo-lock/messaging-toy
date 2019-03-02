package com.lockdown.messaging.cluster.framwork;

public interface MessageWritable<M> {


    public void writeMessage(M message);
}
