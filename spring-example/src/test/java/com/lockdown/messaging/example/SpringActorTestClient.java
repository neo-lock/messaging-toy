package com.lockdown.messaging.example;

import com.lockdown.messaging.cluster.utils.IPUtils;
import com.lockdown.messaging.example.message.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpringActorTestClient {

    public static void main(java.lang.String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new JsonMessageDecoder(),new JsonMessageEncoder(),new SpringActorHandler());
            }
        });


        try {

            ChannelFuture channelFuture = bootstrap.connect(IPUtils.getLocalIP(), 8082).sync();

            for (int i = 0; i < 1; i++) {
                executorService.execute(() -> channelFuture.channel().writeAndFlush(JsonMessage.wrap(new RegisterMessage("123"))));
            }


            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
