package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.ServerEventListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractServer<T extends ServerContext> implements LocalServer<T> {


    private ServerBootstrap bootstrap;
    private T serverContext;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private List<ServerEventListener> eventListeners = new ArrayList<>();


    @Override
    public void addEventListener(ServerEventListener... listeners) {
        if (Objects.nonNull(listeners)) {
            eventListeners.addAll(Arrays.asList(listeners));
        }
    }


    @Override
    public final LocalServer<T> initializer(T serverContext) {
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
        }
    }

    public void startServer() throws InterruptedException {
        try {
            bootstrap.bind(serverContext.getProperties().getNodePort()).sync();
            fireStartEvent();
            syncWait();
        } finally {
            fireStopEvent();
            release();
        }
    }


    private void syncWait() throws InterruptedException {
        countDownLatch.await();
    }

    protected ChannelInitializer<SocketChannel> channelInitializer(T serverContext) {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(providerHandler(serverContext).toArray(new ChannelHandler[0]));
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

    protected abstract List<ChannelHandler> providerHandler(T serverContext);

    protected abstract void optionSetup(ServerBootstrap bootstrap);


    private void fireStopEvent() {
        final LocalServer localServer = this;
        eventListeners.forEach(serverEventListener -> serverEventListener.serverStop(localServer));
    }

    public void stop() {
        countDownLatch.countDown();
    }


    private void release() {
        serverContext.shutdownContext();
    }

    private void fireStartEvent() {
        final LocalServer localServer = this;
        eventListeners.forEach(serverEventListener -> serverEventListener.serverStartup(localServer, serverContext));
    }


}
