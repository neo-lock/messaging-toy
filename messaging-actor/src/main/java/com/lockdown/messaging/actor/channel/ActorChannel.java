package com.lockdown.messaging.actor.channel;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;

public interface ActorChannel extends Channel {

    ChannelPipeline pipeline();

    ActorDestination destination();

}
