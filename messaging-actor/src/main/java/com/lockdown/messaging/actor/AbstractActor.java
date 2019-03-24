package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.channel.ActorChannel;

import java.util.Objects;

public abstract class AbstractActor implements Actor {

    private ActorChannel actorChannel;

    public void setActorChannel(ActorChannel actorChannel) {
        if(Objects.isNull(actorChannel)){
            throw new NullPointerException(" actor channel can't be null !");
        }
        if(Objects.nonNull(this.actorChannel)){
            throw new IllegalStateException(" actor channel set already !");
        }
        this.actorChannel = actorChannel;
    }

    @Override
    public void writeMessage(ActorDestination destination, Object message, boolean autoWrite) {
        actorChannel.writeAndFlush(destination, message, autoWrite);
    }

    @Override
    public void writeMessage(ActorDestination destination, Object message) {
        writeMessage(destination,message,false);
    }

    @Override
    public void writeMessage(Object message) {
        actorChannel.writeAndFlush(message);
    }

    protected ActorDestination getActorDestination() {
        return (ActorDestination) actorChannel.destination();
    }


    @Override
    public ActorDestination destination() {
        if(Objects.nonNull(actorChannel)){
            return (ActorDestination) actorChannel.destination();
        }
        return null;
    }

    @Override
    public ActorChannel channel() {
        return actorChannel;
    }
}
