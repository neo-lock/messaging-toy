package com.lockdown.messaging.actor.channel;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelFactory;

public interface ActorChannelGroup extends ChannelFactory<ActorChannel,ServerDestination> {


    void addActorChannel(ActorChannel actorChannel);


    ActorChannel getChannel(Destination destination);


}
