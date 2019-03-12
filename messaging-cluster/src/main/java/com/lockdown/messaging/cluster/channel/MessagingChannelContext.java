package com.lockdown.messaging.cluster.channel;

public interface MessagingChannelContext extends MessagingChannelInbound {

    ChannelHandler handler();

}
