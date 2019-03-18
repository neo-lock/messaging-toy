package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeRegister extends AbstractNodeCommand implements RegisterNature {

    public NodeRegister() {
        super();
    }

    public NodeRegister(ServerDestination source) {
        super(source);
    }

    @Override
    public CommandType type() {
        return DefaultCommandType.REGISTER_ASK;
    }


}
