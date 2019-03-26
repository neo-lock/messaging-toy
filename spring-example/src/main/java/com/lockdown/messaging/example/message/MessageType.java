package com.lockdown.messaging.example.message;

public enum MessageType {
    REGISTER(RegisterMessage.class), TEXT(TextMessage.class), COMMUNITY(CommunityMessage.class);

    private Class<?> clazz;

    MessageType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static MessageType stringValueOf(String type) {
        for (MessageType t : values()) {
            if (t.name().equals(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException(" can't supported type " + type);
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
