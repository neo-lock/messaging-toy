package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;

public interface NodeMonitorSlot {

    void receivedCommand(RemoteNode remoteNode, SourceNodeCommand msg);


    void inactive(RemoteNode remoteNode);


    void exceptionCaught(RemoteNode remoteNode, Throwable cause);

}
