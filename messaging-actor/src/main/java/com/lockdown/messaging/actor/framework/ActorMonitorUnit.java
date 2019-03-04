package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.cluster.framwork.MessageAcceptor;
import com.lockdown.messaging.cluster.framwork.MonitorUnit;

public interface ActorMonitorUnit extends MonitorUnit<Actor>, MessageAcceptor<Actor, Object> {

}
