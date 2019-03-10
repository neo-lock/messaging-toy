package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.*;

public interface NodeMessageRouter extends MessageEmitter<ServerDestination, SourceNodeCommand>, MessageTrigger<RemoteNode, SourceNodeCommand, NodeMessageAcceptor> {

    void notifyMessage(SourceNodeCommand command, ServerDestination... ignores);

    void sendRandomTarget(SourceNodeCommand command);

}
