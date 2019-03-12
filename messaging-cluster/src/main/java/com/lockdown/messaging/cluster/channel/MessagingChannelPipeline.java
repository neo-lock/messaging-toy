package com.lockdown.messaging.cluster.channel;

public interface MessagingChannelPipeline extends MessagingChannelInbound {

    MessagingChannelPipeline addLast(ChannelHandler handler);

    MessagingChannel channel();

}
