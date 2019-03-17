package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.cluster.channel.support.NodeChannel;
import com.lockdown.messaging.cluster.reactor.support.RemoteNodeChannelInitializer;

public class ActorRemoteNodeChannelInitializer extends RemoteNodeChannelInitializer {

    @Override
    public void initialize(NodeChannel channel) {
        channel.pipeline().addLast(new ActorNodeCommandSplitter());
    }
}
