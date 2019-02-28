package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;

public interface CommandRouter extends CommandAcceptor, CommandTrigger {

    void sendCommand(ServerDestination destination, SourceNodeCommand nodeCommand);


    void notifyCommand(SourceNodeCommand command, ServerDestination... ignores);


    void sendRandomTarget(SourceNodeCommand command);

}
