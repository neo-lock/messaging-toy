package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeRegisterForward extends AbstractNodeCommand {


    private ServerDestination target;

    public NodeRegisterForward() {
    }


    public NodeRegisterForward(ServerDestination source, ServerDestination target) {
        super(source);
        this.target = target;
    }

    public ServerDestination getTarget() {
        return target;
    }

    public void setTarget(ServerDestination target) {
        this.target = target;
    }

    @Override
    public CommandType type() {
        return CommandType.REGISTER_FORWARD;
    }


}
