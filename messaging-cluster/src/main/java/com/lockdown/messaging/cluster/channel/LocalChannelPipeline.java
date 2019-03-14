package com.lockdown.messaging.cluster.channel;

public interface LocalChannelPipeline extends ChannelPipeline<LocalChannelHandler> {


    LocalChannelPipeline addLast(LocalChannelHandler handler);

    LocalChannel channel();

}
