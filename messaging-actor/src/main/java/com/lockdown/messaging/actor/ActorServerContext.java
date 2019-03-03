package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.framework.ActorBeanFactory;
import com.lockdown.messaging.actor.framework.ActorMessageRouter;
import com.lockdown.messaging.actor.framework.ActorMonitor;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerProperties;
import io.netty.channel.Channel;


public interface ActorServerContext<T extends ServerProperties> extends ServerContext<T> {


    ActorMonitor actorMonitor();

    ActorMessageRouter actorMessageRouter();

    ActorBeanFactory actorBeanFactory();

    ActorDestination createActorDestination(Channel channel);
}
