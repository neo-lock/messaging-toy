package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.SyncCommandHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import java.util.Arrays;
import java.util.List;

public class ClusterLocalServer extends AbstractServer<ClusterServerContext> {


    @Override
    protected List<ChannelHandler> providerHandler(ClusterServerContext serverContext) {
        return Arrays.asList(new NodeCommandDecoder(serverContext),
                new NodeCommandEncoder(serverContext),
                new SyncCommandHandler(serverContext),
                new LocalNodeCommandHandler(serverContext));
    }


    @Override
    protected void optionSetup(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }
}
