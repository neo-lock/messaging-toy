package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.framwork.ChannelSlotMonitor;

import java.util.Collection;

public interface ActorMonitor extends ChannelSlotMonitor<Actor, ActorDestination>, ActorMonitorUnit, ActorMessageTrigger {


    Collection<ActorDestination> allActors();

}
