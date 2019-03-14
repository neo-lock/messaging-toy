package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.channel.NodeChannelFactory;
import com.lockdown.messaging.cluster.reactor.NodeChannelFactoryGroup;
import io.netty.channel.ChannelFuture;

public class DefaultNodeChannelFactoryGroup extends AbstractNodeChannelGroup implements NodeChannelFactoryGroup {


    private final Object lock = new Object();
    private final NodeChannelFactory nodeChannelFactory;

    public DefaultNodeChannelFactoryGroup(NodeChannelFactory nodeChannelFactory) {
        this.nodeChannelFactory = nodeChannelFactory;
    }

    @Override
    public NodeChannel getMasterNodeChannel(Destination destination) {
        NodeChannel channel = null;
        synchronized (lock) {
            if (contains(destination)) {
                channel = (NodeChannel) getNodeChannel(destination);
            } else {
                channel = newInstance((ServerDestination) destination);
            }
        }
        return channel;
    }

    @Override
    public NodeChannel newInstance(ChannelFuture channel, ServerDestination destination) {
        NodeChannel nodeChannel = nodeChannelFactory.newInstance(channel, destination);
        addNodeChannel(nodeChannel);
        return nodeChannel;
    }

    @Override
    public NodeChannel newInstance(ServerDestination destination) {
        NodeChannel nodeChannel = nodeChannelFactory.newInstance(destination);
        addNodeChannel(nodeChannel);
        return nodeChannel;
    }


}
