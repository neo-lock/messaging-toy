package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.utils.IPUtils;

public class ClusterServerApplication {

    public static void main(String[] args) {
        ClusterProperties properties = new ClusterProperties();
        properties.setBossThreads(1);
        properties.setWorkerThreads(4);
        properties.setEnableSync(true);
        properties.setMaster(new ServerDestination(IPUtils.getLocalIP(), 9090));
        properties.setMonitorEnable(true);
        properties.setMonitorSeconds(10);
        properties.setNodeWhiteList("909.*");
        properties.setNodePort(9093);
        ClusterServerContext serverContext = new ClusterServerContext(properties);
        ClusterLocalServer localServer = new ClusterLocalServer();
        localServer.initializer(serverContext).start();

    }


}
