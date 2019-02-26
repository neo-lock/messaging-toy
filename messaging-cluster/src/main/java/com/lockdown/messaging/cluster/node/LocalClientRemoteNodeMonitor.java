package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.LocalClient;

public interface LocalClientRemoteNodeMonitor extends RemoteNodeMonitor,LocalClient {


    void initLocalClient(InitializedCallback<LocalClientRemoteNodeMonitor> initializedCallbacks);

}
