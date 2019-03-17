package com.lockdown.messaging.actor.codec;

public class RegisterMessage implements BusinessMessage {


    private String id;

    public RegisterMessage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public MessageType getType() {
        return MessageType.REGISTER;
    }
}
