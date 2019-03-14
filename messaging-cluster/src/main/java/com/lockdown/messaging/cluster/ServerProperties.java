package com.lockdown.messaging.cluster;

public interface ServerProperties {


    int getNodePort();

    ServerDestination getMaster();

    int getBossThreads();

    int getWorkerThreads();

    boolean isMonitorEnable();

    int getMonitorSeconds();

    boolean isEnableSync();

    String getNodeWhiteList();
}
