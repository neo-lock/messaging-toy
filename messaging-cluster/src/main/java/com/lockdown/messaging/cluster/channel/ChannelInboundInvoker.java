package com.lockdown.messaging.cluster.channel;

public interface ChannelInboundInvoker {

    void fireChannelReceived(Object message);

}
