package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.ServerEventListener;

import java.util.concurrent.Future;

public interface LocalServer<T extends ServerContext> {

    public LocalServer<T> initializer(T serverContext);

    public Future<?> start();

    public void stop();

    void addEventListener(ServerEventListener... listeners);
}
