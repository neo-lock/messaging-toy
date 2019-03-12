package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.ServerEventListener;

public interface LocalServer<T extends ServerContext> {

    public LocalServer<T> initializer(T serverContext);

    public void start();

    public void stop();

    void addEventListener(ServerEventListener... listeners);
}
