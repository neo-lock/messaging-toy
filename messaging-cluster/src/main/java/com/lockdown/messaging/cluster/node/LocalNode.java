package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;

public interface LocalNode {


    void registerToCluster(ServerDestination destination);

    boolean isMonitored();

    void monitor(ServerDestination destination);

    void attachTo(ServerDestination destination);

    boolean isAttached();

    boolean monitorCompareAndSet(ServerDestination old, ServerDestination update);

    boolean attachedCompareAndSet(ServerDestination old, ServerDestination update);

    void registerRandomNode();

    void printNodes();

    ServerDestination destination();

}
