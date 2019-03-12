package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.RemoteChannelInitializer;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannel;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannelFactory;
import com.lockdown.messaging.cluster.node.LocalClient;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.ChannelFuture;

public class DefaultRemoteNodeChannelFactory implements RemoteNodeChannelFactory {


    private final ChannelEventLoop eventLoop;
    private final RemoteChannelInitializer channelInitializer;
    private final LocalClient localClient;

    public DefaultRemoteNodeChannelFactory(ChannelEventLoop eventLoop, LocalClient localClient) {
        this.eventLoop = eventLoop;
        this.channelInitializer = new DefaultRemoteChannelInitializer();
        this.localClient = localClient;
    }


    @Override
    public RemoteNodeChannel newInstance(ChannelFuture channel, ServerDestination destination) {
        DefaultRemoteNodeChannel nodeChannel = new DefaultRemoteNodeChannel(eventLoop, channel, destination);
        channelInitializer.initialize(nodeChannel);
        return nodeChannel;
    }

    @Override
    public RemoteNodeChannel newInstance(ServerDestination destination) {
        ChannelFuture channel = localClient.connect(destination);
        return newInstance(channel, destination);
    }
}
