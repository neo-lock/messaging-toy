package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitoredInvoker extends AbstractNodeCommandInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    NodeMonitoredInvoker(Channel channel) {
        super(channel);
    }


    @Override
    public CommandType supportType() {
        return CommandType.MONITORED;
    }


    @Override
    public void executeCommand(LocalNode invoke, SourceNodeCommand command) {
        logger.info(" 注册成功，成功被监视 ! {}", command.getSource());
        if (invoke.isAttached()) {
            logger.warn(" current node monitored,ignore command !");
            return;
        }
        invoke.attachTo(command.getSource());
    }
}
