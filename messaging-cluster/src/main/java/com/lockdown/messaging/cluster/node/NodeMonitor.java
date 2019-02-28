package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;

import java.util.Collection;


public interface NodeMonitor extends CommandTrigger,RemoteNodeBeanFactory {


    RemoteNode getRemoteNode(ServerDestination destination);


    Collection<RemoteNode> AllRemoteNodes();


    RemoteNode randomNode();


    void shutdown();

    void printNodes();
}
