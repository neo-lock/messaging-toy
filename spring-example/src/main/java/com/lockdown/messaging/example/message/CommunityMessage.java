package com.lockdown.messaging.example.message;

public class CommunityMessage implements BusinessMessage {

    private String receiverId;
    private String text;

    public CommunityMessage(String receiverId, String text) {
        this.text = text;
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public MessageType getType() {
        return MessageType.COMMUNITY;
    }
}
