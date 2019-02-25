package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public abstract class AbstractNodeCommand implements NodeCommand {

    protected ServerDestination source;

    public AbstractNodeCommand(){}

    public AbstractNodeCommand(ServerDestination source) {
        this.source = source;
    }

    public void setSource(ServerDestination source) {
        this.source = source;
    }

    public ServerDestination getSource() {
        return source;
    }
}
