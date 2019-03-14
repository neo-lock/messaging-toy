package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.ServerDestination;

public interface NodeChannel extends Channel {

    NodeChannelPipeline pipeline();

    void writeAndFlush(Object obj);

    void close();

}
