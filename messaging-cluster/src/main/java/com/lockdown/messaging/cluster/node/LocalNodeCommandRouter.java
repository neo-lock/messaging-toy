package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;


public interface LocalNodeCommandRouter {

    void sendCommand(ServerDestination destination, SourceNodeCommand nodeCommand);


    void notifyCommand(SourceNodeCommand command, ServerDestination... ignores);


    void sendRandomTarget(SourceNodeCommand command);


    void registerCommandAcceptor(CommandAcceptor acceptor);

}
