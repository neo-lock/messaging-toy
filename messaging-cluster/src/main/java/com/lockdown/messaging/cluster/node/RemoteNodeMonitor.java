package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import com.sun.istack.internal.NotNull;
import java.util.Collection;

/**
 * 节点的监控
 */
public interface RemoteNodeMonitor extends ServerNodeEventListener,LocalServerEventListener {


    RemoteServerNode getRemoteNode(ServerDestination destination);

    Collection<RemoteServerNode> remoteNodes();

    void registerCommandHandler(CommandAcceptor acceptor);

    void commandForward(RemoteServerNode remoteServerNode, NodeCommand command);

    RemoteServerNode randomNode();


    void printNodes();
}
