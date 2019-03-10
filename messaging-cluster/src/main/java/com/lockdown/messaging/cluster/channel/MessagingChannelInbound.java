package com.lockdown.messaging.cluster.channel;

public interface MessagingChannelInbound {


    void fireChannelRegistered();


    void fireChannelReceived(Object message);


    void fireChannelClosed();


    void fireExceptionCaught(Throwable throwable);

}
