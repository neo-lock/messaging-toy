package com.lockdown.messaging.cluster.channel;

public interface ChannelInitializer<T extends Channel> {

    void initialize(T channel);

}
