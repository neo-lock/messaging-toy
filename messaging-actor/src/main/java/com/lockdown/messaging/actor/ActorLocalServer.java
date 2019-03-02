package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.handler.ActorCodecHandler;
import com.lockdown.messaging.actor.handler.ActorTestHandler;
import com.lockdown.messaging.cluster.AbstractServer;
import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.SyncCommandHandler;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import java.util.Arrays;
import java.util.List;

public class ActorLocalServer extends AbstractServer<ActorServerContext> {


    @Override
    protected ServerBootstrap initServerBootstrap(ActorServerContext serverContext) {
        ServerBootstrap bootstrap = super.initServerBootstrap(serverContext);
        ActorProperties properties = (ActorProperties) serverContext.getProperties();
        try {
            bootstrap.bind(IPUtils.getLocalIP(), properties.getActorPort()).sync();
        } catch (InterruptedException e) {
            throw new MessagingInterruptedException(e);
        }
        return bootstrap;
    }

    @Override
    protected List<ChannelHandler> providerHandler(ActorServerContext serverContext) {
        return Arrays.asList(
                new NodeCommandDecoder(serverContext),
                new NodeCommandEncoder(serverContext),
                new SyncCommandHandler(serverContext),
                new LocalNodeCommandHandler(serverContext),
                new ActorCodecHandler(),
                new ActorTestHandler()
        );
    }

    @Override
    protected void optionSetup(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }
}
