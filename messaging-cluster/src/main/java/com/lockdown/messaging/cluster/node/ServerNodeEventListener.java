package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;

public interface ServerNodeEventListener extends CommandAcceptor {

    void nodeRegistered(RemoteServerNode remoteServerNode, NodeCommand command);

    void inactive(ServerDestination destination);

}
