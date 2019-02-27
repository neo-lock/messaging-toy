package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.exception.MessagingNoNodeException;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeClosedInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.CLOSED;
    }

    @Override
    public void executeCommand(LocalNode invoke, RemoteNode remote, NodeCommand command) {
        invoke.monitorCompareAndSet(remote.destination(), null);
        if (invoke.attachedCompareAndSet(remote.destination(), null)) {
            logger.info(" 重新注册 ");
            try {
                invoke.registerRandomNode();
            } catch (MessagingNoNodeException ex) {
                //ignore
            }
        }
    }
}
