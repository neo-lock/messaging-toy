package com.lockdown.messaging.cluster.command;

public enum  DefaultCommandType implements CommandType{

    REGISTER_ASK((short) 1),REGISTER_FORWARD((short) 2),GREETING((short) 3),
    MONITORED((short) 4),CLOSED((short) 9);


    private short type;

    DefaultCommandType(short type) {
        this.type = type;
    }

    @Override
    public Short getType() {
        return type;
    }
}
