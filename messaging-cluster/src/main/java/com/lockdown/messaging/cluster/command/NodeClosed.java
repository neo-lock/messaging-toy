package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeClosed extends AbstractNodeCommand implements ClusterNature{


    public NodeClosed() {
        super();
    }

    public NodeClosed(ServerDestination source) {
        super(source);
    }


    @Override
    public CommandType type() {
        return DefaultCommandType.CLOSED;
    }
}
