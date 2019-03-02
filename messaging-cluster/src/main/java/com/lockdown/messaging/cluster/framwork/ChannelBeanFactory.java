package com.lockdown.messaging.cluster.framwork;
import io.netty.channel.ChannelFuture;

public interface ChannelBeanFactory<T extends ChannelSlot, D extends Destination> {

    T getInstance(ChannelFuture channelFuture, D destination);


}
