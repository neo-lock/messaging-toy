package com.lockdown.messaging.cluster.framwork;

public interface MonitorUnit<T> {

    void inactive(T slot);

    void exceptionCaught(T slot, Throwable cause);


}
