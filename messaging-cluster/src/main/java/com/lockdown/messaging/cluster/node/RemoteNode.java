package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.MessageWritable;

public interface RemoteNode extends ChannelSlot<ServerDestination,SourceNodeCommand> {

    public void applyDestination(ServerDestination destination);

}
