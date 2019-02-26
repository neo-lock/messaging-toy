package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.*;
import com.lockdown.messaging.cluster.node.LocalServerNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterForwardInvoker implements NodeCommandInvoker<LocalServerNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_FORWARD;
    }

    @Override
    public void executeCommand(LocalServerNode local, RemoteNode remote, NodeCommand command) {
        NodeRegisterForward registerForward = (NodeRegisterForward) command;
        if (local.isMonitored()) {
            logger.info(" 当前节点已经监控，打个招呼");
            local.sendCommand(registerForward.getSource(), new NodeGreeting(local.destination()));
        } else {
            local.sendCommand(registerForward.getSource(), new NodeMonitored(local.destination()));
            local.monitor(registerForward.getSource());
        }
    }
}
