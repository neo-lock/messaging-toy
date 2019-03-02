package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.framwork.SlotBeanFactory;

public interface ClusterNodeBeanFactory extends SlotBeanFactory<RemoteNode, ServerDestination> {

    RemoteNode getInstance(ServerDestination destination);

}
