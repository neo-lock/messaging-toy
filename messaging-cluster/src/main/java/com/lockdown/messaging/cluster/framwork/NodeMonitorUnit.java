package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.RemoteNode;

public interface NodeMonitorUnit extends MonitorUnit<RemoteNode>, MessageAcceptor<RemoteNode, SourceNodeCommand> {

}
