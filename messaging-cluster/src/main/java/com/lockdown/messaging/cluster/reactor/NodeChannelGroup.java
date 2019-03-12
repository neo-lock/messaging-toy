package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannel;

import java.util.Collection;


public interface NodeChannelGroup {

    void addNodeChannel(MessagingChannel channel);

    void removeNodeChannel(Destination destination);

    RemoteNodeChannel getMasterNodeChannel(Destination destination);

    RemoteNodeChannel randomNodeChannel();

    Collection<MessagingChannel> nodeChannels();

    MessagingChannel getNodeChannel(Destination source);
}
