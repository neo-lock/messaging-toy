package com.lockdown.messaging.cluster.channel;

public interface ChannelInBound {

    void fireChannelReceived(Object message);

}
