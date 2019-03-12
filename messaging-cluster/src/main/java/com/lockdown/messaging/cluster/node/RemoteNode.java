package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;

public interface RemoteNode {

    void applyDestination(ServerDestination destination);
}
