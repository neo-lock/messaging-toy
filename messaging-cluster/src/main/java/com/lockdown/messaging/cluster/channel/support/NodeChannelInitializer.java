package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.ChannelInitializer;
import com.lockdown.messaging.cluster.channel.NodeChannel;

public class NodeChannelInitializer implements ChannelInitializer<NodeChannel> {


    @Override
    public void initialize(NodeChannel channel) {
        channel.pipeline().addLast(new RemoteNodeCommandSplitter());
    }
}
