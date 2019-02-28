package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;

public interface CommandAcceptor {

    void acceptedCommand(RemoteNode serverNode, SourceNodeCommand command);

}
