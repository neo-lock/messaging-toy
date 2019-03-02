package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.utils.IPUtils;

public class ActorServerApplication {

    public static void main(String[] args) {
        ActorProperties properties = new ActorProperties();
        properties.setBossThreads(1);
        properties.setWorkerThreads(4);
        properties.setEnableSync(true);
        properties.setMaster(new ServerDestination(IPUtils.getLocalIP(), 9090));
        properties.setMonitorEnable(true);
        properties.setMonitorSeconds(10);
        properties.setNodePort(9090);
        properties.setActorPort(8080);

//        ClusterServerContext serverContext = new ClusterServerContext(properties);
//        serverContext.initContext();
//        ClusterLocalServer localServer = new ClusterLocalServer();
//        try {
//            localServer.initializer(serverContext).start();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            localServer.stop();
//        }
        ActorServerContext context = new ActorServerContext(properties);
        ActorLocalServer actorLocalServer = new ActorLocalServer();
        try {
            actorLocalServer.initializer(context).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
            actorLocalServer.stop();
        }
    }
}
