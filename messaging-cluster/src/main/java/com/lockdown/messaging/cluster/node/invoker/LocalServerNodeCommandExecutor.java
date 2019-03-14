package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalServerNodeCommandExecutor extends AbstractNodeCommandInvoker {

    private ConcurrentMap<CommandType, NodeCommandInvoker<LocalNode>> invokerContext = new ConcurrentHashMap<>();
    private Logger logger = LoggerFactory.getLogger(getClass());

    LocalServerNodeCommandExecutor(Channel channel) {
        super(channel);
    }


    public void registerInvoker(NodeCommandInvoker<LocalNode> invoker) {
        if (invokerContext.containsKey(invoker.supportType())) {
            throw new IllegalStateException(" invoke command type already set !");
        }
        this.invokerContext.putIfAbsent(invoker.supportType(), invoker);
    }


    @Override
    public void executeCommand(LocalNode local, SourceNodeCommand command) {
        if (!invokerContext.containsKey(command.type())) {
            logger.info(" unsupported current command {}, discard !", command.type());
            return;
        }
        logger.info(" 执行命令处理器 {}", command.type());
        invokerContext.get(command.type()).executeCommand(local, command);
    }

}
