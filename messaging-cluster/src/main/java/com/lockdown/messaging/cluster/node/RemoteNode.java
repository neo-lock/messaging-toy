package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;

public interface RemoteNode extends ChannelSlot<ServerDestination, SourceNodeCommand> {

    void applyDestination(ServerDestination destination);
}
