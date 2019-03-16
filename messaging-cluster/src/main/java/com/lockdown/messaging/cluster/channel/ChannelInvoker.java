package com.lockdown.messaging.cluster.channel;

public interface ChannelInvoker {

    void fireExceptionCaught(Throwable throwable);

    void fireChannelClosed();
}
