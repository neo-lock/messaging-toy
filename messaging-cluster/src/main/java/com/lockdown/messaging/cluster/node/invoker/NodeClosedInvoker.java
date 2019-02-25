package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.LocalServerNode;
import com.lockdown.messaging.cluster.node.RemoteServerNode;

public class NodeClosedInvoker implements NodeCommandInvoker<LocalServerNode> {


    @Override
    public CommandType supportType() {
        return CommandType.CLOSED;
    }

    @Override
    public void executeCommand(LocalServerNode invoke, RemoteServerNode remote, NodeCommand command) {
        invoke.matchClearMonitor(remote.destination());

        if (invoke.matchClearAttached(remote.destination())) {
            //随机注册
        }
    }
}
