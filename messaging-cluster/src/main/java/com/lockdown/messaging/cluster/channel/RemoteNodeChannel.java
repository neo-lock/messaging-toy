package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.ServerDestination;

public interface RemoteNodeChannel extends MessagingChannel {

    MessagingChannelPipeline pipeline();

    void writeAndFlush(Object obj);

    void close();

    ServerDestination destination();

}
