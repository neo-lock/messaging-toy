package com.lockdown.messaging.actor;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.utils.IPUtils;

public class ActorServerApplication {


    public static void main(String[] args){
        SimpleActorProperties properties = new SimpleActorProperties();
        properties.setBossThreads(1);
        properties.setWorkerThreads(4);
        properties.setEnableSync(true);
        properties.setMaster(new ServerDestination(IPUtils.getLocalIP(), 9090));
        properties.setMonitorEnable(true);
        properties.setMonitorSeconds(10);
        properties.setNodeWhiteList("909.*");
        properties.setNodePort(9093);

        ActorServerContext serverContext = new ActorServerContext(properties);
        ActorServer localServer = new ActorServer();
        localServer.initializer(serverContext).start();
    }


}
