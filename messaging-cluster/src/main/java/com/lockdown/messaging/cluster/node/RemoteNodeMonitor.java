package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelFuture;

import java.util.Collection;

/**
 * 节点的监控
 */
public interface RemoteNodeMonitor extends RemoteNodeForwardSlot {


    RemoteNode getRemoteNode(ServerDestination destination);

    Collection<RemoteNode> AllRemoteNodes();

    RemoteNode randomNode();

    void closeNode(RemoteNode remoteNode);

    RemoteNode newRemoteNodeInstance(ChannelFuture channelFuture);

    RemoteNode newRemoteNodeInstance(ChannelFuture channelFuture, ServerDestination destination);

    void printNodes();
}
