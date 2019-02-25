package com.lockdown.messaging.cluster.exception;

public class MessagingException extends RuntimeException {

    public MessagingException(Throwable throwable) {
        super(throwable);
    }

    public MessagingException(String message) {
        super(message);
    }

}
