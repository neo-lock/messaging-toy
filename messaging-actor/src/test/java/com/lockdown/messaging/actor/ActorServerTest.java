package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ServerDestination;

public class ActorServerTest {

    public static void main(String[] args) {

        SimpleActorProperties properties = new SimpleActorProperties();
        properties.setBossThreads(1);
        properties.setWorkerThreads(4);
        properties.setEnableSync(true);
        properties.setMaster(new ServerDestination("127.0.0.1", 9090));
        properties.setMonitorEnable(true);
        properties.setMonitorSeconds(10);
        properties.setNodeWhiteList("909.*");
        properties.setNodePort(9092);
        properties.setActorClassName(TestActor.class.getName());
        properties.setActorCodecClassName(TestActorCodec.class.getName());
        ActorServerContext serverContext = new ActorServerContext(properties);
        ActorServer localServer = new ActorServer(9093);
        localServer.initializer(serverContext).start();


    }


}
