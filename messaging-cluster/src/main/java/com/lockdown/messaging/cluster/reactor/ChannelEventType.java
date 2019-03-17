package com.lockdown.messaging.cluster.reactor;

public enum ChannelEventType {


    REGISTER_MASTER,
    RANDOM_REGISTER,
    NODE_READ,

    EXCEPTION,
    CHANNEL_WRITE,
    CHANNEL_READ,
    CHANNEL_CLOSE,
    CHANNEL_NOTIFY,

}
