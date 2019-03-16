package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.Destination;
import io.netty.channel.ChannelFuture;

public interface ChannelFactory<T extends Channel, D extends Destination> {


    T newInstance(ChannelFuture future, D destination);

}
