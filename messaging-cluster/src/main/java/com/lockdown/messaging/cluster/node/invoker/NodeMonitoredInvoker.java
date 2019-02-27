package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitoredInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.MONITORED;
    }

    @Override
    public void executeCommand(LocalNode invoke, RemoteNode remote, NodeCommand command) {
        if (invoke.isAttached()) {
            logger.warn(" current node monitored,ignore command !");
            return;
        }
        invoke.attachTo(remote.destination());
    }
}
