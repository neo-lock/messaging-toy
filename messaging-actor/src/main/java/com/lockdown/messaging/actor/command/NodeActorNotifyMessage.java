package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.cluster.command.AbstractNodeCommand;
import com.lockdown.messaging.cluster.command.CommandType;

public class NodeActorNotifyMessage extends AbstractNodeCommand {

    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public CommandType type() {
        return ActorCommandType.ACTOR_NOTIFY;
    }
}
