package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class LocalServerNodeInitializer implements LocalServerEventListener {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void serverStartup(LocalServer localServer, MessagingNodeContext serverContext) {
        logger.info(" 本地端口启动成功 ");
        RemoteNodeMonitor monitor = new DefaultRemoteNodeMonitor(serverContext);
        serverContext.registerEventHandler(monitor);
        LocalServerNode localServerNode = new DefaultLocalServerNode(monitor, (ServerDestination) serverContext.getLocalDestination());
        logger.info(" 连接集群");
        if (Objects.nonNull(serverContext.getClusterDestination()) && !serverContext.getLocalDestination().equals(serverContext.getClusterDestination())) {
            localServerNode.registerToCluster((ServerDestination) serverContext.getClusterDestination());
        }
    }

}
