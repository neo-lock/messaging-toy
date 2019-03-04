package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.framwork.SlotMonitoringBeanFactory;

public interface ActorMonitoringBeanFactory extends
        SlotMonitoringBeanFactory<Actor, ActorDestination, ActorMonitor>,
        ActorBeanFactory {

}
