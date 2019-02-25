package com.lockdown.messaging.core;

import com.lockdown.messaging.core.event.MessagingServerEventListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class DefaultMessagingServer implements  MessagingServer{


    private MessagingServerEventListener messagingServerEventListener;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private CountDownLatch countDownLatch;

    public DefaultMessagingServer(MessagingServerEventListener eventListener){
        this.messagingServerEventListener = eventListener;
    }

    public void startLocalServer() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        countDownLatch = new CountDownLatch(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DispatcherChannel());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture innerChannel = b.bind(9090).sync();
            ChannelFuture outerChannel = b.bind(8100).sync();
            triggerEvent();
            //innerChannel.channel().closeFuture().sync();
            countDownLatch.await();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void release(){
        if(null!=bossGroup){
            bossGroup.shutdownGracefully();
        }
        if(null!=workerGroup){
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void start() {
        try {
            startLocalServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        release();
        Thread.currentThread().interrupt();

    }

    @Override
    public void triggerEvent() throws InterruptedException {
        if(null!=messagingServerEventListener){
            messagingServerEventListener.serverStarted(this);
        }
    }
}
