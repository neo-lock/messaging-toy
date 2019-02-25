package com.lockdown.messaging.cluster.exception;

public class MessagingHostException extends MessagingException {

    public MessagingHostException(Throwable throwable) {
        super(throwable);
    }

    public MessagingHostException(String s) {
        super(s);
    }
}
