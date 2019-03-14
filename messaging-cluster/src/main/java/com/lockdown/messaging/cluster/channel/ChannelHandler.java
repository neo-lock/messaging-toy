package com.lockdown.messaging.cluster.channel;

public interface ChannelHandler<T extends ChannelContext> {

    void channelReceived(T ctx, Object message);


}
