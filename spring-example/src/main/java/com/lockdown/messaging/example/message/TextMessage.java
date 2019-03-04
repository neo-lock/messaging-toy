package com.lockdown.messaging.example.message;

public class TextMessage implements BusinessMessage {


    private String text;


    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public MessageType getType() {
        return MessageType.TEXT;
    }
}
