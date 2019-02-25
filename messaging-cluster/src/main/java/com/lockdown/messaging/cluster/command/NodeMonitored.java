package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeMonitored extends AbstractNodeCommand implements RegisterNature {

    public NodeMonitored() {
        super();
    }

    public NodeMonitored(ServerDestination source) {
        super(source);
    }


    @Override
    public CommandType type() {
        return CommandType.MONITORED;
    }


}
