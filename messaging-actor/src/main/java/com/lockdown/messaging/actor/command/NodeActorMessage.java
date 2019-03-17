package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.command.AbstractNodeCommand;
import com.lockdown.messaging.cluster.command.CommandType;

public class NodeActorMessage extends AbstractNodeCommand {

    private byte[] content;
    private String channelId;
    //发送人的信息
    private ActorDestination destination;

    public NodeActorMessage(){}

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setDestination(ActorDestination destination) {
        this.destination = destination;
    }

    @Override
    public CommandType type() {
        return CommandType.ACTOR_MSG;
    }

    public byte[] getContent() {
        return content;
    }

    public ActorDestination getActorDestination() {
        return destination;
    }
}
