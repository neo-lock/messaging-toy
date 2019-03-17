package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.channel.support.NodeChannel;
import com.lockdown.messaging.cluster.channel.support.NodeCommandSplitter;

public class RemoteNodeChannelInitializer implements NodeChannelInitializer {
    @Override
    public void initialize(NodeChannel channel) {
        channel.pipeline().addLast(new NodeCommandSplitter());
    }
}
