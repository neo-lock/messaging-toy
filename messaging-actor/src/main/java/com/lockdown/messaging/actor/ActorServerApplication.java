package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.framework.JsonMessageCodec;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.utils.IPUtils;

public class ActorServerApplication {

    public static void main(String[] args) {
//        ActorProperties properties = new ActorProperties();
//        properties.setBossThreads(1);
//        properties.setWorkerThreads(4);
//        properties.setEnableSync(true);
//        properties.setMaster(new ServerDestination(IPUtils.getLocalIP(), 9090));
//        properties.setMonitorEnable(true);
//        properties.setMonitorSeconds(10);
//        properties.setNodePort(9090);
//        properties.setActorPort(8080);
//        properties.setNodeWhiteList("909.*");
//        properties.setActorClassName(TestActor.class.getName());
//        properties.setActorCodecClassName(JsonMessageCodec.class.getName());
//
//        ClusterActorServerContext<ActorProperties> serverContext = new ClusterActorServerContext<>(properties);
//        ActorLocalServer actorLocalServer = new ActorLocalServer();
//        actorLocalServer.initializer(serverContext).start();

    }
}
