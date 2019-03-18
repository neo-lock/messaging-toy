package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.channel.support.SimpleActorFactory;
import com.lockdown.messaging.actor.codec.ActorHandler;
import com.lockdown.messaging.actor.codec.JsonMessageDecoder;
import com.lockdown.messaging.actor.codec.JsonMessageEncoder;
import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelHandler;

import java.util.Arrays;
import java.util.List;

public class ActorServerTest {

    public static void main(String[] args) {

        SimpleActorProperties properties = new SimpleActorProperties();
        properties.setBossThreads(1);
        properties.setWorkerThreads(4);
        properties.setEnableSync(true);
        properties.setMaster(new ServerDestination("127.0.0.1", 9090));
        properties.setMonitorEnable(false);
        properties.setMonitorSeconds(10);
        properties.setNodeWhiteList("909.*");
        properties.setNodePort(9091);
        properties.setActorFactoryClassName(SimpleActorFactory.class.getName());
        properties.setActorClassName(TestActor.class.getName());
        properties.setActorCodecClassName(TestActorCodec.class.getName());
        ActorServerContext serverContext = new ActorServerContext(properties);
        ActorServer localServer = new ActorServer(8081);
        localServer.customHandler(serverContext1 -> Arrays.asList(
                new JsonMessageDecoder(),
                new JsonMessageEncoder(),
                new ActorHandler()
        )).initializer(serverContext).start();


    }


}
