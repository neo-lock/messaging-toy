package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannel;
import com.lockdown.messaging.cluster.exception.MessagingHostException;
import com.lockdown.messaging.cluster.reactor.NodeChannelGroup;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractNodeChannelGroup implements NodeChannelGroup {

    private ConcurrentMap<Destination, MessagingChannel> nodeChannelMap = new ConcurrentHashMap<>();

    @Override
    public void addNodeChannel(MessagingChannel channel) {
        nodeChannelMap.putIfAbsent(channel.destination(), channel);
    }

    @Override
    public void removeNodeChannel(Destination destination) {
        nodeChannelMap.remove(destination);
    }

    @Override
    public RemoteNodeChannel randomNodeChannel() {
        if (nodeChannelMap.isEmpty()) {
            throw new MessagingHostException("no host!");
        }
        Optional<MessagingChannel> channelOptional = nodeChannelMap.values().stream().findFirst();
        if (!channelOptional.isPresent()) {
            throw new MessagingHostException(" no host !");
        }
        return (RemoteNodeChannel) channelOptional.get();
    }

    @Override
    public Collection<MessagingChannel> nodeChannels() {
        return nodeChannelMap.values();
    }

    @Override
    public RemoteNodeChannel getNodeChannel(Destination destination) {
        return (RemoteNodeChannel) nodeChannelMap.get(destination);
    }

    boolean contains(Destination destination) {
        return nodeChannelMap.containsKey(destination);
    }
}
