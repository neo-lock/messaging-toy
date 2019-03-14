package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelFuture;

public interface NodeChannelFactory {

    NodeChannel newInstance(ChannelFuture channel, ServerDestination destination);


    NodeChannel newInstance(ServerDestination destination);

}
