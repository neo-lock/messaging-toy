package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.AbstractActor;
import com.lockdown.messaging.actor.ActorFactory;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.cluster.exception.MessagingException;


public class SimpleActorFactory implements ActorFactory {

    @Override
    public AbstractActor newInstance(ActorChannel actorChannel) {
        try {
            AbstractActor actor = (AbstractActor) ((ActorServerContext)actorChannel.eventLoop().serverContext()).actorClass().newInstance();
            actor.setActorChannel(actorChannel);
            return actor;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MessagingException(e.getMessage());
        }
    }

}
