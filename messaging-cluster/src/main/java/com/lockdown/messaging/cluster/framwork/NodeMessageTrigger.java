package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.RemoteNode;

public interface NodeMessageTrigger extends MessageTrigger<RemoteNode,SourceNodeCommand,NodeMessageAcceptor> {

}
