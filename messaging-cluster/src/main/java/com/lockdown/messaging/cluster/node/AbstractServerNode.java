package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;

public abstract class AbstractServerNode implements ServerNode {


    protected ServerDestination destination;

    @Override
    public ServerDestination destination() {
        return destination;
    }

}
