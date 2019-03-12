package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.exception.MessagingDestinationNotFoundException;
import com.lockdown.messaging.cluster.node.LocalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeClosedInvoker extends AbstractNodeCommandInvoker {

    private Logger logger = LoggerFactory.getLogger(getClass());

    NodeClosedInvoker(MessagingChannel channel) {
        super(channel);
    }

    @Override
    public CommandType supportType() {
        return CommandType.CLOSED;
    }


    @Override
    public void executeCommand(LocalNode invoke, SourceNodeCommand command) {
        logger.info("收到node close !");
        getChannel().eventLoop().nodeChannelGroup().removeNodeChannel(command.getSource());
        invoke.monitorCompareAndSet(command.getSource(), null);
        if (invoke.attachedCompareAndSet(command.getSource(), null)) {
            logger.info(" 重新注册 ");
            try {
                invoke.registerRandomNode();
            } catch (MessagingDestinationNotFoundException ex) {
                //ignore
            }
        }
    }
}
