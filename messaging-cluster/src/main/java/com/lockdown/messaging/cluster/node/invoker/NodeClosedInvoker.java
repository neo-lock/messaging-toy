package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.LocalServerNode;
import com.lockdown.messaging.cluster.node.RemoteServerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeClosedInvoker implements NodeCommandInvoker<LocalServerNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.CLOSED;
    }

    @Override
    public void executeCommand(LocalServerNode invoke, RemoteServerNode remote, NodeCommand command) {
        invoke.monitorCompareAndSet(remote.destination(),null);
        if (invoke.attachedCompareAndSet(remote.destination(),null)) {
            logger.info(" 重新注册 ");
            invoke.registerRandomNode();
            //随机注册
        }
    }
}
