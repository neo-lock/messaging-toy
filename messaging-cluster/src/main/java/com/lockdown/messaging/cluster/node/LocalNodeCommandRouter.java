package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;


public interface LocalNodeCommandRouter {

    void sendCommand(ServerDestination destination, NodeCommand nodeCommand);


    void notifyCommand(NodeCommand command, ServerDestination... ignores);


    void sendRandomTarget(NodeCommand command);


    void registerCommandAcceptor(CommandAcceptor acceptor);

}
