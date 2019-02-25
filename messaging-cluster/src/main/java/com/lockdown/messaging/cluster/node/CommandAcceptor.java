package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.command.NodeCommand;

public interface CommandAcceptor {

    void commandEvent(RemoteServerNode serverNode, NodeCommand command);

}
