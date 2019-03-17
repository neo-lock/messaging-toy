package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.channel.ActorChannel;

public abstract class AbstractActor  implements Actor {

    private ActorChannel actorChannel;

    public void setActorChannel(ActorChannel actorChannel) {
        this.actorChannel = actorChannel;
    }

    @Override
    public void writeMessage(ActorDestination destination, Object message) {
        actorChannel.writeAndFlush(destination,message);
    }

    @Override
    public void writeMessage(Object message) {
        actorChannel.writeAndFlush(message);
    }

    protected ActorDestination getActorDestination(){
        return (ActorDestination) actorChannel.destination();
    }



}
