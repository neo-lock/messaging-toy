package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.command.NodeCommand;

public interface CommandAcceptor {

    void commandEvent(RemoteNode serverNode, NodeCommand command);

}
