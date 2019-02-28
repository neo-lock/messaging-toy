package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelFuture;

public interface LocalClient {


    ChannelFuture connect(ServerDestination destination);

}
