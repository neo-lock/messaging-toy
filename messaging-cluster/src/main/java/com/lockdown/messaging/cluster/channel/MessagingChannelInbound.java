package com.lockdown.messaging.cluster.channel;

public interface MessagingChannelInbound {


    void fireChannelReceived(Object message);

    void fireChannelRegistered();

    void fireChannelClosed();

    void fireExceptionCaught(Throwable throwable);

}
