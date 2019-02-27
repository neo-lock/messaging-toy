package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class LocalServerNodeCommandExecutor implements NodeCommandExecutor<LocalNode> {

    private Map<CommandType, NodeCommandInvoker<LocalNode>> invokerContext = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(getClass());

    public LocalServerNodeCommandExecutor() {
    }

    public void registerInvoker(NodeCommandInvoker<LocalNode> invoker) {
        if (invokerContext.containsKey(invoker.supportType())) {
            throw new IllegalStateException(" invoke command type already set !");
        }
        this.invokerContext.putIfAbsent(invoker.supportType(), invoker);
    }

    public void setInvokerContext(Map<CommandType, NodeCommandInvoker<LocalNode>> invokerContext) {
        this.invokerContext = invokerContext;
    }


    @Override
    public void executeCommand(LocalNode local, RemoteNode remote, NodeCommand command) {
        if (!invokerContext.containsKey(command.type())) {
            logger.info(" unsupported current command {}, discard !", command.type());
            return;
        }
        invokerContext.get(command.type()).executeCommand(local, remote, command);
    }

}
