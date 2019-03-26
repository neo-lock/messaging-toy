package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.codec.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ActorClientTest {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new JsonMessageDecoder())
                        .addLast(new JsonMessageEncoder())
                        .addLast(new ActorHandler());
            }
        });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8081).sync();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("发送注册消息!");
                channelFuture.channel().writeAndFlush(JsonMessage.wrap(new RegisterMessage(UUID.randomUUID().toString())));
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("发送测试消息!");
                channelFuture.channel().writeAndFlush(JsonMessage.wrap(new TextMessage("测试消息")));
            }
        }).start();
        countDownLatch.await();


    }

}
