package com.lockdown.messaging.cluster;

import io.netty.channel.ChannelFuture;

public interface LocalClient {

    ChannelFuture connect(ServerDestination source);

    ServerDestination localDestination();

}
