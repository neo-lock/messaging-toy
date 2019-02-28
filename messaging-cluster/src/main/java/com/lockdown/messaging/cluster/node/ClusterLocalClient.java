package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.sockethandler.ChannelInitializerProvider;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClusterLocalClient implements LocalClient {


    private Bootstrap bootstrap;

    ClusterLocalClient(ServerContext serverContext, ChannelInitializerProvider provider) {
        this.init(serverContext, provider);
    }

    private void init(ServerContext serverContext, ChannelInitializerProvider provider) {
        bootstrap = new Bootstrap();
        bootstrap.group(serverContext.contextExecutor().getBoss());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(provider.provideChannelInitializer());
    }


    @Override
    public ChannelFuture connect(ServerDestination source) {
        try {
            return bootstrap.connect(source.getIpAddress(), source.getPort()).sync();
        } catch (InterruptedException e) {
            throw new MessagingInterruptedException(e);
        }
    }

}

