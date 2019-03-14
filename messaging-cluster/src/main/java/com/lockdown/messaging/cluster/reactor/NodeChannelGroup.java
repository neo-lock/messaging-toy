package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.Channel;

import java.util.Collection;


public interface NodeChannelGroup {

    void addNodeChannel(Channel channel);

    void removeNodeChannel(Destination destination);

    Channel getMasterNodeChannel(Destination destination);

    Channel randomNodeChannel();

    Collection<Channel> nodeChannels();

    Channel getNodeChannel(Destination source);

}
