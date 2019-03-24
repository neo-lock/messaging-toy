package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.command.AbstractNodeCommand;
import com.lockdown.messaging.cluster.command.CommandType;

public class NodeActorPushMessage extends AbstractNodeCommand {

    private byte[] content;
    private ActorDestination destination;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public ActorDestination getDestination() {
        return destination;
    }

    public void setDestination(ActorDestination destination) {
        this.destination = destination;
    }

    @Override
    public CommandType type() {
        return ActorCommandType.ACTOR_PUSH;
    }
}
