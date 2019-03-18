package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.cluster.command.CommandType;

public enum ActorCommandType implements CommandType {
    ACTOR_MSG((short)6), ACTOR_NOTIFY((short)7);

    private short type;

    ActorCommandType(short type) {
        this.type = type;
    }

    @Override
    public Short getType() {
        return type;
    }
}
