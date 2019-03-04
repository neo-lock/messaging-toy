package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.ServerEventListener;
import com.lockdown.messaging.cluster.node.ClusterNodeMonitor;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.NodeMessageRouter;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;
import java.util.regex.Pattern;


public interface ServerContext<T extends ServerProperties> extends ServerEventListener {


    T getProperties();

    RuntimeEnvironment runtimeEnvironment();

    ClusterNodeMonitor nodeMonitor();

    ContextExecutor contextExecutor();

    void shutdownContext();

    ServerDestination localDestination();

    NodeMessageRouter commandRouter();

    Pattern nodeWhiteList();

    LocalNode localNode();

}
