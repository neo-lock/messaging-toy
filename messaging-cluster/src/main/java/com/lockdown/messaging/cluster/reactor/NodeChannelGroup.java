package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.framwork.Destination;

public interface NodeChannelGroup {

    boolean addChannel(MessagingChannel channel);

    void removeChannel(Destination destination);

    MessagingChannel getChannel(Destination destination);


}
