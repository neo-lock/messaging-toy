package com.lockdown.messaging.actor.exception;

import com.lockdown.messaging.cluster.exception.MessagingException;

public class ActorNotFoundException extends MessagingException {


    public ActorNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public ActorNotFoundException(String message) {
        super(message);
    }
}
