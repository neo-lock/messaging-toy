package com.lockdown.messaging.cluster.channel;

public interface ChannelOutboundInvoker {

    void fireChannelWrite(Object message);

}
