package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelFuture;

public interface RemoteNodeBeanFactory {


    RemoteNode getNodeInstance(ChannelFuture channelFuture, ServerDestination destination);


    RemoteNode getNodeInstance(ServerDestination destination);


}
