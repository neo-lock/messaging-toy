package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.MessageRouter;
import com.lockdown.messaging.cluster.framwork.MessageTrigger;

public interface NodeMessageRouter extends MessageRouter<RemoteNode, ServerDestination, SourceNodeCommand>,MessageTrigger<RemoteNode, SourceNodeCommand> {

    void notifyMessage(SourceNodeCommand command, ServerDestination... ignores);

    void sendRandomTarget(SourceNodeCommand command);

}
