package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeGreeting;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterForwardInvoker extends AbstractNodeCommandInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    NodeRegisterForwardInvoker(Channel channel) {
        super(channel);
    }


    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_FORWARD;
    }

    @Override
    public void executeCommand(LocalNode local, SourceNodeCommand command) {
        logger.info("收到注册转发");
        NodeRegisterForward registerForward = (NodeRegisterForward) command;
        if (local.isAttached()) {
            if (local.attachedCompareAndSet(registerForward.getSource(), registerForward.getTarget())) {
                logger.debug("当前节点成功替换依赖对象,开始进行重新注册 {}", registerForward.getTarget());
                local.registerToCluster(registerForward.getTarget());
            } else {
                logger.debug("发送GREETING {}", local.destination());
                ((NodeChannel) getChannel().eventLoop().nodeChannelGroup().getNodeChannel(registerForward.getTarget())).writeAndFlush(new NodeGreeting(local.destination()));
            }
        } else {
            logger.debug("当前对象没有attached,开始向{}注册", registerForward.getTarget());
            local.registerToCluster(registerForward.getTarget());
        }
    }
}
