package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.ServerEventListener;
import com.lockdown.messaging.cluster.framwork.ClusterNodeMonitor;
import com.lockdown.messaging.cluster.framwork.MessageRouter;
import com.lockdown.messaging.cluster.node.ClusterNodeBeanFactory;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;

import java.util.regex.Pattern;


public interface ServerContext extends ServerEventListener {


    ServerProperties getProperties();

    RuntimeEnvironment runtimeEnvironment();

    void shutdownContext();

    ClusterNodeMonitor nodeMonitor();

    ContextExecutor contextExecutor();

    void initContext();

    ServerDestination localDestination();

    MessageRouter commandRouter();

    Pattern nodeWhiteList();

}
