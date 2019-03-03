package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.framwork.MessageEmitter;

public interface ActorMessageEmitter extends MessageEmitter<ActorDestination, Object> {

}
