package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;

public class SimpleNodeMonitorFactory implements NodeMonitorFactory {


    private final ServerContext serverContext;

    public SimpleNodeMonitorFactory(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public ClusterNodeMonitor getInstance() {
        ClusterMonitoringNodeBeanFactory nodeBeanFactory = new ClusterMonitoringNodeBeanFactory(serverContext);
        return new ClusterNodeMonitor(nodeBeanFactory);
    }
}
