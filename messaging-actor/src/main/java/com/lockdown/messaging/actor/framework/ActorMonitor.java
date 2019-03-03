package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.framwork.ChannelSlotMonitor;

public interface ActorMonitor extends ChannelSlotMonitor<Actor, ActorDestination>, ActorMonitorUnit, ActorMessageTrigger {

}
