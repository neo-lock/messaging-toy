package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import com.lockdown.messaging.cluster.node.LocalServerNodeInitializer;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.utils.GlobalTimer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClusterLocalServer implements LocalServer {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private final MessagingNodeContext serverContext;
    private Set<LocalServerEventListener> eventListeners = new HashSet<>();


    public ClusterLocalServer(MessagingNodeContext serverContext) {
        this.serverContext = serverContext;
        this.registerDefaultListener();
    }

    private void registerDefaultListener(){
        eventListeners.add(new LocalServerNodeInitializer());
        eventListeners.add(new GlobalTimer());
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
                            socketChannel.pipeline().addLast(new LocalNodeCommandHandler(serverContext.getEventHandler()));
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
        eventListeners.forEach(localServerEventListener -> serverContext.executeRunnable(() -> localServerEventListener.serverStop(localServer)));
    }

    public void stop() {
        countDownLatch.countDown();
    }

    private void release(){
        serverContext.shutdownExecutor();
    }

    private void triggerStartupEvent() {
        logger.info(" server startup ============ ");
        final LocalServer localServer = this;
        eventListeners.forEach(localServerEventListener -> serverContext.executeRunnable(() -> localServerEventListener.serverStartup(localServer,serverContext)));
    }
}
