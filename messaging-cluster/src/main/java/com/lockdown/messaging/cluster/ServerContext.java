package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.command.CommandCodecHandler;
import com.lockdown.messaging.cluster.event.ServerEventListener;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.NodeChannelInitializer;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;

import java.util.regex.Pattern;


public interface ServerContext<T extends ServerProperties> extends ServerEventListener {


    T getProperties();

    RuntimeEnvironment runtimeEnvironment();

    ContextExecutor contextExecutor();

    void shutdownContext();

    ServerDestination localDestination();

    Pattern nodeWhiteList();

    LocalNode localNode();

    ChannelEventLoop channelEventLoop();

    NodeChannelInitializer nodeChannelInitializer();

    CommandCodecHandler codecHandler();

    ServerContext check();


}
