package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.MessageRouter;

import com.lockdown.messaging.cluster.framwork.NodeMessageTrigger;

public interface NodeMessageRouter extends MessageRouter<RemoteNode, ServerDestination, SourceNodeCommand>, NodeMessageTrigger {

    void notifyMessage(SourceNodeCommand command, ServerDestination... ignores);

    void sendRandomTarget(SourceNodeCommand command);

}
