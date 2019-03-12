package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;


public interface RemoteNodeChannelFactory {

    public RemoteNodeChannel newInstance(ChannelFuture channel, ServerDestination destination);


    public RemoteNodeChannel newInstance(ServerDestination destination);


}
