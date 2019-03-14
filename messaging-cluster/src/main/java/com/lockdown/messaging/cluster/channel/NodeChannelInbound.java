package com.lockdown.messaging.cluster.channel;

public interface NodeChannelInbound extends ChannelInBound {

    void fireChannelClosed();

    void fireExceptionCaught(Throwable throwable);

}
