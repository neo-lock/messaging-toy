package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;

public interface RemoteServerNode extends ServerNode {


    void sendCommand(NodeCommand command);


    void applyDestination(ServerDestination destination);



}
