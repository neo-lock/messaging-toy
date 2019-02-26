package com.lockdown.messaging.cluster.exception;

public class MessagingNoNodeException extends MessagingException {

    public MessagingNoNodeException(Throwable throwable) {
        super(throwable);
    }

    public MessagingNoNodeException(String message){
        super(message);
    }
}
