package com.lockdown.messaging.actor.channel;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelFactory;

import java.util.Collection;

public interface ActorGroup extends ChannelFactory<ActorChannel, ServerDestination> {


    void addActor(Actor actor);


    Actor getActor(Destination destination);


    Collection<Actor> allActors();


    void printNodes();

    void removeActor(Destination destination);
}
