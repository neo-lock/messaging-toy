package com.lockdown.messaging.cluster.exception;

import com.lockdown.messaging.cluster.ServerDestination;

public class MessagingNodeNotFoundException extends MessagingException {

    public MessagingNodeNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public MessagingNodeNotFoundException(ServerDestination destination) {
        super(destination + " node not found !");
    }

    public MessagingNodeNotFoundException(String message) {
        super(message);
    }
}
