package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import com.lockdown.messaging.cluster.node.LocalServerNodeInitializer;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeIdleHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class ClusterLocalServer implements LocalServer {


    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private MessagingNodeContext serverContext;
    private LocalServerEventListener eventListener;
    private LocalClient localClient;


    public ClusterLocalServer(MessagingNodeContext serverContext) {
        this.serverContext = serverContext;
        this.serverContext.setLocalServer(this);
        this.eventListener = new LocalServerNodeInitializer();
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
                            socketChannel.pipeline().addLast(new NodeIdleHandler(10, 10));
                            socketChannel.pipeline().addLast(new LocalNodeCommandHandler(serverContext.getEventHandler()));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.bind(serverContext.getProperties().getNodePort()).sync();
            initLocalClient();
            countDownLatch.await();
        } finally {
            serverContext.shutdownExecutor();
        }
    }

    public void stop() {
        countDownLatch.countDown();
    }

    private void initLocalClient(){
        localClient = new ClusterLocalClient(serverContext);
        triggerStartupEvent();
    }

    private void triggerStartupEvent() {
        if (Objects.nonNull(eventListener)) {
            eventListener.serverStartup(this, this.serverContext);
        }
    }
}
