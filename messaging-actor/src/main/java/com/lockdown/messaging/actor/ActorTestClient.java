package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.handler.ActorCodecHandler;
import com.lockdown.messaging.actor.handler.ActorTestHandler;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActorTestClient {

    public static void main(java.lang.String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ActorCodecHandler(), new ActorTestHandler());
            }
        });


        try {
            ChannelFuture channelFuture = bootstrap.connect(IPUtils.getLocalIP(), 8080).sync();
            for (int i = 0; i < 1; i++) {
                executorService.execute(() -> channelFuture.channel().writeAndFlush("测试消息==========="));
            }

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
