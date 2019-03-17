package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.ServerEventListener;
import io.netty.channel.ChannelHandler;

public interface LocalServer<T extends ServerContext> {

    LocalServer<T> initializer(T serverContext);

    void start();

    void stop();

    void addEventListener(ServerEventListener... listeners);
}
