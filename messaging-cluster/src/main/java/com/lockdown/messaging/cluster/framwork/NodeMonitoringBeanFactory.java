package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.node.ClusterNodeBeanFactory;
import com.lockdown.messaging.cluster.node.RemoteNode;

public interface NodeMonitoringBeanFactory extends ChannelMonitoringBeanFactory<RemoteNode,ServerDestination,NodeForwardSlot>,ClusterNodeBeanFactory {
}
