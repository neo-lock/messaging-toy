package com.lockdown.messaging.cluster;


import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.event.ServerEventListener;
import com.lockdown.messaging.cluster.node.CommandRouter;
import com.lockdown.messaging.cluster.support.Recoverable;
import com.lockdown.messaging.cluster.node.RemoteNodeBeanFactory;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;
import net.sf.cglib.proxy.MethodProxy;

import java.util.concurrent.CountDownLatch;

public interface ServerContext extends ServerEventListener {


    ServerProperties getProperties();

    RuntimeEnvironment runtimeEnvironment();

    void shutdownContext();

    RemoteNodeBeanFactory nodeBeanFactory();

    ContextExecutor contextExecutor();

    void initContext();

    ServerDestination localDestination();

    CommandRouter commandRouter();

}
