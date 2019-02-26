package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class ClusterLocalServer implements LocalServer {

    private final MessagingNodeContext serverContext;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private List<LocalServerEventListener> eventListeners = new ArrayList<>();


    public ClusterLocalServer(MessagingNodeContext serverContext) {
        this.serverContext = serverContext;

    }

    @Override
    public void addEventListener(LocalServerEventListener... listeners) {
        if (Objects.nonNull(listeners)) {
            eventListeners.addAll(Arrays.asList(listeners));
        }
    }


    public void start() throws InterruptedException {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(serverContext.getBossGroup(), serverContext.getWorkerGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NodeCommandDecoder(serverContext.getProperties().getNodePort()), new NodeCommandEncoder(serverContext.getProperties().getNodePort()));
                            socketChannel.pipeline().addLast(new LocalNodeCommandHandler(serverContext));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.bind(serverContext.getProperties().getNodePort()).sync();
            triggerStartupEvent();
            countDownLatch.await();
        } finally {
            triggerStopEvent();
            release();
        }
    }

    private void triggerStopEvent() {
        final LocalServer localServer = this;
        eventListeners.forEach(localServerEventListener -> localServerEventListener.serverStop(localServer));
    }

    public void stop() {
        countDownLatch.countDown();
    }


    private void release() {
        serverContext.shutdownExecutor();
    }

    private void triggerStartupEvent() {

        final LocalServer localServer = this;
        eventListeners.forEach(localServerEventListener -> localServerEventListener.serverStartup(localServer, serverContext));
    }
}
