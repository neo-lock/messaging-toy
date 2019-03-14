package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.exception.MessagingHostException;
import com.lockdown.messaging.cluster.reactor.NodeChannelGroup;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractNodeChannelGroup implements NodeChannelGroup {

    private Map<Destination, Channel> nodeChannelMap = new ConcurrentHashMap<>();

    @Override
    public void addNodeChannel(Channel channel) {
        nodeChannelMap.put(channel.destination(), channel);
    }

    @Override
    public void removeNodeChannel(Destination destination) {
        nodeChannelMap.remove(destination);
    }

    @Override
    public NodeChannel randomNodeChannel() {
        if (nodeChannelMap.isEmpty()) {
            throw new MessagingHostException("no host!");
        }
        NodeChannel nodeChannel = null;
        for (Channel channel : nodeChannelMap.values()) {
            if (NodeChannel.class.isAssignableFrom(channel.getClass())) {
                nodeChannel = (NodeChannel) channel;
                break;
            }
        }
        if (null == nodeChannel) {
            throw new MessagingHostException(" no host !");
        }
        return nodeChannel;
    }

    @Override
    public Collection<Channel> nodeChannels() {
        return nodeChannelMap.values();
    }

    @Override
    public Channel getNodeChannel(Destination destination) {
        return nodeChannelMap.get(destination);
    }

    boolean contains(Destination destination) {
        return nodeChannelMap.containsKey(destination);
    }
}
