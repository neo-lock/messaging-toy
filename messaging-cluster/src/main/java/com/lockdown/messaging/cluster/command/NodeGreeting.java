package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeGreeting extends AbstractNodeCommand {


    public NodeGreeting(){}

    public NodeGreeting(ServerDestination source) {
        super(source);
    }

    @Override
    public CommandType type() {
        return CommandType.GREETING;
    }

}
