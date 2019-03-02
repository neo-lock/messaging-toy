package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeGreeting;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterForwardInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_FORWARD;
    }

    @Override
    public void executeCommand(LocalNode local, RemoteNode remote, NodeCommand command) {
        NodeRegisterForward registerForward = (NodeRegisterForward) command;
        if (local.isAttached()) {
            local.printNodes();
            if (local.attachedCompareAndSet(registerForward.getSource(), registerForward.getTarget())) {
                logger.debug("当前节点成功替换依赖对象,开始进行重新注册 {}", registerForward.getTarget());
                local.registerToCluster(registerForward.getTarget());
            } else {
                logger.debug("发送GREETING {}",local.destination());
                local.sendCommand(registerForward.getTarget(), new NodeGreeting(local.destination()));
            }
        } else {
            logger.debug("当前对象没有attached,开始向{}注册", registerForward.getTarget());
            local.registerToCluster(registerForward.getTarget());
        }

    }
}
