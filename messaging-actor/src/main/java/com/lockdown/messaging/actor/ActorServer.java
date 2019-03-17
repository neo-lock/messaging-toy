package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.sockethandler.ActorSocketHandler;
import com.lockdown.messaging.cluster.AbstractServer;
import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.SyncCommandHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActorServer extends AbstractServer<ActorServerContext> {



    @Override
    protected List<ChannelHandler> providerHeadHandler(ActorServerContext serverContext) {
                return Arrays.asList(
                new NodeCommandDecoder(serverContext),
                new NodeCommandEncoder(serverContext),
                new LocalNodeCommandHandler(serverContext));
    }

    @Override
    protected List<ChannelHandler> providerTailHandler(ActorServerContext serverContext) {
        return Collections.singletonList(new ActorSocketHandler(serverContext));
    }

    @Override
    protected void optionSetup(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);

    }

}
