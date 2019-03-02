package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.Destination;
import io.netty.channel.ChannelFuture;

public interface ChannelBeanFactory<T extends ChannelSlot, D extends Destination> {

    T getInstance(ChannelFuture channelFuture, D destination);

}
