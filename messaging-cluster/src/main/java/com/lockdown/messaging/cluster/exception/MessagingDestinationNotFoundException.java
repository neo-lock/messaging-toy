package com.lockdown.messaging.cluster.exception;

import com.lockdown.messaging.cluster.framwork.Destination;

public class MessagingDestinationNotFoundException extends MessagingException {

    public MessagingDestinationNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public MessagingDestinationNotFoundException(Destination destination) {
        super(destination + " node not found !");
    }

    public MessagingDestinationNotFoundException(String message) {
        super(message);
    }
}
