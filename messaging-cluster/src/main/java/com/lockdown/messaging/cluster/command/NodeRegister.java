package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeRegister extends AbstractNodeCommand {

    public NodeRegister(){
        super();
    }

    public NodeRegister(ServerDestination source) {
        super(source);
    }

    @Override
    public CommandType type() {
        return CommandType.REGISTER_ASK;
    }
}
