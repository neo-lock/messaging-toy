package com.lockdown.messaging.cluster;

public interface MessagingProperties {


    int getNodePort();


    ServerDestination masterTarget();


    int getBossThreads();

    int getWorkerThreads();

    boolean nodeMonitorEnable();

    long nodeMonitorSeconds();

    boolean isEnableSync();
}
