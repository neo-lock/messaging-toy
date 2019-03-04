package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.AbstractNodeCommand;
import com.lockdown.messaging.cluster.command.CommandType;

public class NodeActorCommand extends AbstractNodeCommand {


    private final String receiverChannel;

    private ActorDestination actorDestination;

    private final byte[] content;

    public NodeActorCommand(ActorDestination actorDestination, String receiverChannel, byte[] content) {
        super(actorDestination.getServerDestination());
        this.receiverChannel = receiverChannel;
        this.actorDestination = actorDestination;
        this.content = content;
    }

    public String getReceiverChannel() {
        return receiverChannel;
    }

    public ActorDestination getActorDestination() {
        return actorDestination;
    }

    public void setActorDestination(ActorDestination actorDestination) {
        this.actorDestination = actorDestination;
    }

    public byte[] getContent() {
        return content;
    }


    @Override
    public CommandType type() {
        return CommandType.ACTOR_MSG;
    }
}
