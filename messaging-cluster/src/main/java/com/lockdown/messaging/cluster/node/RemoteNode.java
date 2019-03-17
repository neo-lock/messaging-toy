package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;

@Deprecated
public interface RemoteNode {

    void applyDestination(ServerDestination destination);
}
