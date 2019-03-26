package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.cluster.command.CommandType;

public enum ActorCommandType implements CommandType {

    ACTOR_MSG((short) 6), ACTOR_PUSH((short) 7), ACTOR_NOTIFY((short) 8);


    private short type;

    ActorCommandType(short type) {
        this.type = type;
    }

    @Override
    public Short getType() {
        return type;
    }
}
