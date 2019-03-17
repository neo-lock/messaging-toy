package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.sockethandler.ActorSocketHandler;
import com.lockdown.messaging.cluster.AbstractServer;
import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class ActorServer extends AbstractServer<ActorServerContext> {


    private final int actorPort;
    private ActorCustomHandlerInitializer handlerInitializer;

    public ActorServer(int actorPort) {
        super();
        this.actorPort = actorPort;

    }

    @Override
    public ActorServer initializer(ActorServerContext serverContext) {
        ActorServer actorServer =  (ActorServer) super.initializer(serverContext);
        Pattern pattern = Pattern.compile(serverContext.getProperties().getNodeWhiteList());
        if(pattern.matcher(String.valueOf(actorPort)).matches()){
            stop();
            throw new MessagingException("本地Actor端口不能与集群处于同一网段!");
        }

        return actorServer;
    }

    @Override
    protected ServerBootstrap initServerBootstrap(ActorServerContext serverContext) {
        ServerBootstrap bootstrap = super.initServerBootstrap(serverContext);
        try {
            bootstrap.bind(actorPort).sync();
            logger.info("绑定端口 {}!",actorPort);
        } catch (InterruptedException e) {
            throw new MessagingInterruptedException(e);
        }
        return bootstrap;
    }

    public ActorServer customHandler(ActorCustomHandlerInitializer handlerInitializer) {
        this.handlerInitializer = handlerInitializer;
        return this;
    }

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
    protected List<ChannelHandler> providerCustomHandler(ActorServerContext serverContext) {
        if (null == this.handlerInitializer) {
            return super.providerCustomHandler(serverContext);
        } else {
            return this.handlerInitializer.initializer(serverContext);
        }
    }

    @Override
    protected void optionSetup(ServerBootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public interface ActorCustomHandlerInitializer {
        List<ChannelHandler> initializer(ActorServerContext serverContext);
    }

}
