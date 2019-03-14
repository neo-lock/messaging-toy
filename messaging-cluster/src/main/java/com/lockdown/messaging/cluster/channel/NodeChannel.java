package com.lockdown.messaging.cluster.channel;

public interface NodeChannel extends Channel {

    NodeChannelPipeline pipeline();

    void writeAndFlush(Object obj);

    void close();

}
