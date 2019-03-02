package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.node.RemoteNode;

public interface NodeSlotMonitor extends ChannelSlotMonitor <RemoteNode,ServerDestination> ,NodeForwardSlot,NodeMessageTrigger {

}
