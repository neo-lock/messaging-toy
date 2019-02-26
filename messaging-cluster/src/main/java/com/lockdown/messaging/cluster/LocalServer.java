package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.LocalServerEventListener;

public interface LocalServer {


    public void start() throws Exception;

    public void stop();

    void addEventListener(LocalServerEventListener... listeners);
}
