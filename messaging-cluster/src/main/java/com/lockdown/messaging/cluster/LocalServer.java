package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import com.lockdown.messaging.cluster.node.RemoteNodeMonitor;
import com.lockdown.messaging.cluster.utils.GlobalTimer;

public interface LocalServer {


    public void start() throws Exception;

    public void stop();

    void addEventListener(LocalServerEventListener...listeners);
}
