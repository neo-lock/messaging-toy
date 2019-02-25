package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.LocalServerNode;
import com.lockdown.messaging.cluster.node.RemoteServerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitoredInvoker implements NodeCommandInvoker<LocalServerNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.MONITORED;
    }

    @Override
    public void executeCommand(LocalServerNode invoke, RemoteServerNode remote, NodeCommand command) {
        if (invoke.isAttached()) {
            logger.warn(" current node monitored,ignore command !");
            return;
        }
        invoke.attachTo(remote.destination());
    }
}
