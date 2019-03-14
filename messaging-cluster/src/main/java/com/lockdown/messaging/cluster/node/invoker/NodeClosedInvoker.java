package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.exception.MessagingDestinationNotFoundException;
import com.lockdown.messaging.cluster.node.LocalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeClosedInvoker extends AbstractNodeCommandInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    NodeClosedInvoker(Channel channel) {
        super(channel);
    }

    @Override
    public CommandType supportType() {
        return CommandType.CLOSED;
    }


    @Override
    public void executeCommand(LocalNode invoke, SourceNodeCommand command) {
        logger.info("开始关闭节点 {}", command.getSource());
        getChannel().eventLoop().nodeChannelGroup().removeNodeChannel(command.getSource());
        invoke.monitorCompareAndSet(command.getSource(), null);
        //如果当前节点是父节点,需要重新注册
        if (invoke.attachedCompareAndSet(command.getSource(), null)) {
            logger.info(" 重新注册 ");
            try {
                invoke.registerRandomNode();
            } catch (MessagingDestinationNotFoundException ex) {
                //ignore
                ex.printStackTrace();
            }
        } else {
            logger.info(" 不进行重新注册!!!!!!!!!!======================================");
        }
    }
}
