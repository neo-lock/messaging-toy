package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;

public interface ServerNodeEventListener extends CommandAcceptor {

    void nodeRegistered(RemoteServerNode remoteServerNode);

    void inactive(ServerDestination destination);

}
