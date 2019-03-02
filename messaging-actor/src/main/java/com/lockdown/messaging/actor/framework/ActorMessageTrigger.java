package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.framwork.MessageTrigger;

public interface ActorMessageTrigger extends MessageTrigger<Actor,ActorDestination,ActorMessageAccepor> {
}
