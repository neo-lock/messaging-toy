package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.sockethandler.ChannelInitializerProvider;
import com.lockdown.messaging.cluster.sockethandler.LocalClientChannelInitializerProvider;
import io.netty.channel.ChannelFuture;

public abstract class AbstractRemoteNodeBeanFactory implements RemoteNodeBeanFactory {


    private LocalClient localClient;
    private final ServerContext serverContext;


    AbstractRemoteNodeBeanFactory(ServerContext serverContext) {
        this.serverContext = serverContext;
        this.initLocalClient(serverContext);
    }

    private void initLocalClient(ServerContext serverContext) {
        ChannelInitializerProvider channelInitializerProvider = new LocalClientChannelInitializerProvider(this, serverContext);
        this.localClient = new ClusterLocalClient(serverContext, channelInitializerProvider);
    }

    protected ServerContext getServerContext() {
        return serverContext;
    }

    @Override
    public RemoteNode getNodeInstance(ServerDestination destination) {
        ChannelFuture channelFuture = localClient.connect(destination);
        return getNodeInstance(channelFuture, destination);
    }

}
