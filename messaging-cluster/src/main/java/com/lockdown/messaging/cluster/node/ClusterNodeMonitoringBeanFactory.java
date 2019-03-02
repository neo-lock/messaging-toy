package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.MessageForwardSlot;

public interface ClusterNodeMonitoringBeanFactory extends ClusterNodeBeanFactory, ChannelMonitoringBeanFactory<RemoteNode, ServerDestination> {


    void setMonitorSlot(MessageForwardSlot<RemoteNode, SourceNodeCommand> monitorSlot);

}
