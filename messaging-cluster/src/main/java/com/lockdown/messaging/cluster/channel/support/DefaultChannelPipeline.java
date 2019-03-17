package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.*;

public class DefaultChannelPipeline extends AbstractChannelPipeline {



    DefaultChannelPipeline(Channel channel) {
        super(channel, new HeadChannelHandler(channel), new TailChannelHandler());
    }



}
