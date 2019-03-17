package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.ServerEventListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractServer<T extends ServerContext> implements LocalServer<T> {


    private ServerBootstrap bootstrap;
    private T serverContext;
    private List<ServerEventListener> eventListeners = new ArrayList<>();
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addEventListener(ServerEventListener... listeners) {
        if (Objects.nonNull(listeners)) {
            eventListeners.addAll(Arrays.asList(listeners));
        }
    }


    @Override
    public LocalServer<T> initializer(T serverContext) {
        this.serverContext = serverContext;
        this.addEventListener(this.serverContext);
        this.bootstrap = initServerBootstrap(this.serverContext);
        return this;
    }

    @Override
    public final void start() {
        try {
            startServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
            stop();
        }
    }

    private void startServer() throws InterruptedException {
        bootstrap.bind(serverContext.getProperties().getNodePort()).sync();
        fireStartEvent();
    }


    protected ChannelInitializer<SocketChannel> channelInitializer(T serverContext) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                List<ChannelHandler> handlers = new ArrayList<>();
                handlers.addAll(providerHeadHandler(serverContext));
                handlers.addAll(providerCustomHandler(serverContext));
                handlers.addAll(providerTailHandler(serverContext));
                socketChannel.pipeline().addLast(handlers.toArray(new ChannelHandler[0]));
            }
        };
    }

    protected ServerBootstrap initServerBootstrap(T serverContext) {
        bootstrap = new ServerBootstrap();
        bootstrap.group(serverContext.contextExecutor().getBoss(), serverContext.contextExecutor().getWorker())
                .channel(NioServerSocketChannel.class);
        bootstrap.childHandler(channelInitializer(serverContext));
        optionSetup(bootstrap);
        return bootstrap;
    }

    protected List<ChannelHandler> providerHeadHandler(T serverContext) {
        return Collections.emptyList();
    }

    protected List<ChannelHandler> providerCustomHandler(T serverContext) {
        return Collections.emptyList();
    }

    protected List<ChannelHandler> providerTailHandler(T serverContext) {
        return Collections.emptyList();
    }


    protected abstract void optionSetup(ServerBootstrap bootstrap);


    private void fireStopEvent() {
        logger.info("fire server stop event !");
        final LocalServer localServer = this;
        eventListeners.forEach(serverEventListener -> serverEventListener.serverStop(localServer));
    }

    public void stop() {
        fireStopEvent();
        release();
    }


    private void release() {
        logger.info(" server context shutdown !");
        serverContext.shutdownContext();
    }


    private void fireStartEvent() {
        final LocalServer localServer = this;
        eventListeners.forEach(serverEventListener -> serverEventListener.serverStartup(localServer, serverContext));
    }


}
