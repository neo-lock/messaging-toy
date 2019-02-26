package com.lockdown.messaging.cluster.node;

public interface RemoteNodeForwardSlot extends RemoteNodeSlot{

    void registerForwardSlot(RemoteNodeSlot slot);

}
