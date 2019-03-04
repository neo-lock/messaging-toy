package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.ActorDestination;

public interface ActorMessageEmitter{

    public void sendMessage(Actor actor, ActorDestination destination, Object obj);

}
