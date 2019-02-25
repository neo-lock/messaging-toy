package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeRegisterForward extends AbstractNodeCommand {

    public NodeRegisterForward() {
    }

    public NodeRegisterForward(ServerDestination source) {
        super(source);
    }

    @Override
    public CommandType type() {
        return CommandType.REGISTER_FORWARD;
    }


}
