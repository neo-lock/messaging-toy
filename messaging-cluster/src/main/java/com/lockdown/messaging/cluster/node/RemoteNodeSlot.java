package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;

public interface RemoteNodeSlot {

    void receivedCommand(RemoteNode remoteNode, NodeCommand msg);


    void inactive(RemoteNode remoteNode);


    void exceptionCaught(RemoteNode remoteNode, Throwable cause);


    void applyDestination(RemoteNode remoteNode, ServerDestination destination);

}
