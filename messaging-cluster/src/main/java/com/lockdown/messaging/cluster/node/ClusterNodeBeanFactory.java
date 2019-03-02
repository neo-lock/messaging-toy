package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.framwork.ChannelBeanFactory;

public interface ClusterNodeBeanFactory extends ChannelBeanFactory<RemoteNode, ServerDestination> {

    RemoteNode getInstance(ServerDestination destination);

}
