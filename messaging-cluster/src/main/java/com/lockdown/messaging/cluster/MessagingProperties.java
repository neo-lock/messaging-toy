package com.lockdown.messaging.cluster;

public interface MessagingProperties {


    public int getNodePort();


    public ServerDestination masterTarget();


    int getBossThreads();

    int getWorkerThreads();

    boolean nodeMonitorEnable();

    long nodeMonitorSeconds();
}
