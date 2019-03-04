package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.framwork.NodeMonitorUnit;
import com.lockdown.messaging.cluster.sockethandler.ChannelInitializerProvider;
import com.lockdown.messaging.cluster.sockethandler.LocalClientChannelInitializerProvider;
import io.netty.channel.ChannelFuture;

public abstract class AbstractClusterNodeBeanFactory extends AbstractMonitoringBeanFactory<RemoteNode, ServerDestination, NodeMonitorUnit> implements ClusterNodeBeanFactory {


    private final ServerContext serverContext;
    private LocalClient localClient;


    AbstractClusterNodeBeanFactory(ServerContext serverContext) {
        this.serverContext = serverContext;
        this.initLocalClient(serverContext);
    }

    private void initLocalClient(ServerContext serverContext) {
        ChannelInitializerProvider channelInitializerProvider = new LocalClientChannelInitializerProvider(serverContext);
        this.localClient = new ClusterLocalClient(serverContext, channelInitializerProvider);
    }

    protected ServerContext getServerContext() {
        return serverContext;
    }


    @Override
    public RemoteNode getInstance(ServerDestination destination) {
        ChannelFuture channelFuture = localClient.connect(destination);
        return getInstance(channelFuture, destination);
    }

}
