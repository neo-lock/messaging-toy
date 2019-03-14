package com.lockdown.messaging.cluster.reactor;

public enum ChannelEventType {


    REGISTER_MASTER, NODE_INACTIVE, RANDOM_REGISTER,
    NODE_READ,
    NODE_CLOSE,


    CHANNEL_WRITE,
    CHANNEL_READ,
    CHANNEL_CLOSE

}
