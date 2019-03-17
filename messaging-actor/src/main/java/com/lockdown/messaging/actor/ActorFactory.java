package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.channel.ActorChannel;

public interface ActorFactory {

    AbstractActor newInstance(ActorChannel actorChannel);

}
