package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannel;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannelFactory;
import com.lockdown.messaging.cluster.reactor.NodeChannelFactoryGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class DefaultNodeChannelFactoryGroup extends AbstractNodeChannelGroup implements NodeChannelFactoryGroup {

    private final RemoteNodeChannelFactory channelFactory;
    private final Object lock = new Object();

    public DefaultNodeChannelFactoryGroup(RemoteNodeChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    @Override
    public RemoteNodeChannel getMasterNodeChannel(Destination destination) {
        RemoteNodeChannel channel = null;
        synchronized (lock) {
            if (contains(destination)) {
                channel = getNodeChannel(destination);
            } else {
                channel = newInstance((ServerDestination) destination);
            }
        }
        return channel;
    }


    @Override
    public RemoteNodeChannel newInstance(ChannelFuture channel, ServerDestination destination) {
        RemoteNodeChannel nodeChannel = channelFactory.newInstance(channel, destination);
        addNodeChannel(nodeChannel);
        return nodeChannel;
    }


    @Override
    public RemoteNodeChannel newInstance(ServerDestination destination) {
        RemoteNodeChannel channel = channelFactory.newInstance(destination);
        addNodeChannel(channel);
        return channel;
    }

}
