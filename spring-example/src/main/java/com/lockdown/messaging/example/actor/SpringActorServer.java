package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorLocalServer;
import com.lockdown.messaging.actor.handler.ActorFinallyHandler;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.SyncCommandHandler;
import com.lockdown.messaging.example.message.JsonMessageDecoder;
import com.lockdown.messaging.example.message.JsonMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import java.util.Arrays;
import java.util.List;

public class SpringActorServer extends ActorLocalServer<SpringActorServerContext> {
    @Override
    protected List<ChannelHandler> providerHandler(SpringActorServerContext serverContext) {
        return Arrays.asList(
                new NodeCommandDecoder(serverContext),
                new NodeCommandEncoder(serverContext),
                new SyncCommandHandler(serverContext),
                new LocalNodeCommandHandler(serverContext),
                new JsonMessageDecoder(),
                new JsonMessageEncoder(),
                new ActorFinallyHandler(serverContext)
        );
    }

    @Override
    protected void optionSetup(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }
}
