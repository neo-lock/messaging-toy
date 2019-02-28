package com.lockdown.messaging.cluster.node;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.ContextExecutor;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommandSegmentRouter implements CommandRouter {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final NodeMonitor nodeMonitor;
    private final ContextExecutor executor;
    private CommandAcceptor acceptor;


    public CommandSegmentRouter(NodeMonitor nodeMonitor, ContextExecutor executor) {
        this.executor = executor;
        this.nodeMonitor = nodeMonitor;
        this.nodeMonitor.registerAcceptor(this);
    }


    @Override
    public void sendCommand(ServerDestination destination, SourceNodeCommand nodeCommand) {
        RemoteNode serverNode = nodeMonitor.getRemoteNode(destination);
        serverNode.sendCommand(nodeCommand);
    }

    @Override
    public void notifyCommand(SourceNodeCommand command, ServerDestination... ignores) {
        Set<ServerDestination> whiteList = new HashSet<>(Arrays.asList(ignores));
        Collection<RemoteNode> nodes = nodeMonitor.AllRemoteNodes();
        nodes.forEach(remoteServerNode -> {
            if (whiteList.contains(remoteServerNode.destination())) {
                return;
            }
            remoteServerNode.sendCommand(command);
        });
    }

    @Override
    public void sendRandomTarget(SourceNodeCommand command) {
        RemoteNode random = nodeMonitor.randomNode();
        random.sendCommand(command);
    }


    @Override
    public void commandTriggered(RemoteNode node, SourceNodeCommand command) {
        logger.info(" command triggered {}", JSON.toJSONString(command));
        acceptor.acceptedCommand(node, command);
    }

    @Override
    public void registerAcceptor(CommandAcceptor acceptor) {
        this.acceptor = acceptor;
    }

    @Override
    public void acceptedCommand(RemoteNode serverNode, SourceNodeCommand command) {
        executor.getSegment().execute(() -> commandTriggered(serverNode, command));
    }

}
