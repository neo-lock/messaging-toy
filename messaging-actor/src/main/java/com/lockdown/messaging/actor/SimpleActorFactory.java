package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.exception.MessagingException;


public class SimpleActorFactory implements ActorFactory {

    private Class<?> actorClass;

    public SimpleActorFactory(Class<?> actorClass) {
        this.actorClass = actorClass;
    }

    @Override
    public AbstractActor newInstance() {
        try {
            return (AbstractActor) actorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MessagingException(e.getMessage());
        }
    }

}
