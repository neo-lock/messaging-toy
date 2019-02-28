package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import io.netty.channel.ChannelId;

public interface RemoteNode extends ServerNode {


    void sendCommand(NodeCommand command);

    void applyDestination(ServerDestination destination);

    void close();

    void receivedCommandEvent(SourceNodeCommand command);

    void inactiveEvent();

    void exceptionCaughtEvent(Throwable cause);

    ChannelId channelId();

}
