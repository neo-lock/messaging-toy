package com.lockdown.messaging.cluster.node;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.NodeCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class DefaultGlobalCommandRouter implements GlobalCommandRouter {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private MessagingNodeContext nodeContext;
    private CommandAcceptor commandAcceptor;

    public DefaultGlobalCommandRouter(MessagingNodeContext nodeContext) {
        this.nodeContext = nodeContext;
    }

    private RemoteNodeMonitor remoteNodeMonitor() {
        return nodeContext.getNodeMonitor();
    }

    @Override
    public void sendCommand(ServerDestination destination, NodeCommand nodeCommand) {
        RemoteNode serverNode = remoteNodeMonitor().getRemoteNode(destination);
        serverNode.sendCommand(nodeCommand);
    }

    @Override
    public void notifyCommand(NodeCommand command, ServerDestination... ignores) {
        Set<ServerDestination> whiteList = new HashSet<>(Arrays.asList(ignores));
        Collection<RemoteNode> nodes = remoteNodeMonitor().AllRemoteNodes();
        nodes.forEach(remoteServerNode -> {
            if (whiteList.contains(remoteServerNode.destination())) {
                return;
            }
            remoteServerNode.sendCommand(command);
        });
    }

    @Override
    public void sendRandomTarget(NodeCommand command) {
        RemoteNode random = remoteNodeMonitor().randomNode();
        random.sendCommand(command);
    }

    @Override
    public void registerCommandAcceptor(CommandAcceptor acceptor) {
        this.commandAcceptor = acceptor;
    }

    @Override
    public void receivedCommand(RemoteNode remoteNode, NodeCommand command) {
        logger.info(" 收到消息 {},{}", command.getClass(), JSON.toJSONString(command));
        nodeContext.executeRunnable(() -> commandAcceptor.commandEvent(remoteNode, command));
    }


    @Override
    public void inactive(RemoteNode remoteNode) {
        receivedCommand(remoteNode, new NodeClosed(remoteNode.destination()));
    }

    @Override
    public void exceptionCaught(RemoteNode remoteNode, Throwable cause) {
        //ignore
    }


    @Override
    public void applyDestination(RemoteNode remoteNode, ServerDestination destination) {
        //ignore
    }

}
