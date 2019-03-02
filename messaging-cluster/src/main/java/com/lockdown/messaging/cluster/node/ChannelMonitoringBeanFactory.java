package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.ChannelBeanFactory;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.Destination;
import com.lockdown.messaging.cluster.framwork.MessageForwardSlot;


public interface ChannelMonitoringBeanFactory<T extends ChannelSlot, D extends Destination> extends ChannelBeanFactory<T, D> {


    void setMonitorSlot(MessageForwardSlot<RemoteNode, SourceNodeCommand> monitorSlot);

}
