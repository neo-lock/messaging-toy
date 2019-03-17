package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelFactory;
import com.lockdown.messaging.cluster.channel.support.NodeChannel;

import java.util.Collection;


public interface NodeChannelGroup extends ChannelFactory<NodeChannel, ServerDestination> {

    void addNodeChannel(NodeChannel channel);

    void removeNodeChannel(Destination destination);

    NodeChannel connectOnNotExists(Destination destination);

    NodeChannel randomNodeChannel();

    Collection<NodeChannel> nodeChannels();

    NodeChannel getNodeChannel(Destination source);

    void printNodes();

    boolean containsNode(Destination destination);

}
