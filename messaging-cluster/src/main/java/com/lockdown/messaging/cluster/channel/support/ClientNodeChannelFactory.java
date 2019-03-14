package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelInitializer;
import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.channel.NodeChannelFactory;
import com.lockdown.messaging.cluster.node.LocalClient;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.ChannelFuture;

public class ClientNodeChannelFactory implements NodeChannelFactory {

    private final ChannelEventLoop eventLoop;
    private final LocalClient localClient;
    private final NodeChannelInitializer channelInitializer;

    public ClientNodeChannelFactory(ChannelEventLoop eventLoop, LocalClient localClient) {
        this.eventLoop = eventLoop;
        this.localClient = localClient;
        this.channelInitializer = new NodeChannelInitializer();
    }


    @Override
    public NodeChannel newInstance(ChannelFuture channel, ServerDestination destination) {
        DefaultNodeChannel nodeChannel = new DefaultNodeChannel(eventLoop, channel, destination);
        this.channelInitializer.initialize(nodeChannel);
        return nodeChannel;
    }

    @Override
    public NodeChannel newInstance(ServerDestination destination) {
        ChannelFuture future = localClient.connect(destination);
        return newInstance(future, destination);
    }
}
