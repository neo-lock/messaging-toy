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
import java.util.concurrent.TimeUnit;

public class SpringActorSenderClient {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new JsonMessageDecoder(), new JsonMessageEncoder(), new SpringActorHandler());
            }
        });


        try {

            ChannelFuture channelFuture = bootstrap.connect(IPUtils.getLocalIP(), 8081).sync();
            for (int i = 0; i < 1; i++) {
                TimeUnit.SECONDS.sleep(1);
                executorService.execute(() -> channelFuture.channel().writeAndFlush(new RegisterMessage("123")));
            }
            executorService.submit((Runnable) () -> {
                for(int i=0;i<10000;i++){
                    channelFuture.channel().writeAndFlush(new CommunityMessage("321", "测试聊天"+i));
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
