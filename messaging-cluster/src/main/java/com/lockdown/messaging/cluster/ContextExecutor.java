package com.lockdown.messaging.cluster;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ContextExecutor {


    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private EventLoopGroup segment;

    public ContextExecutor(ServerProperties properties) {
        this.boss = new NioEventLoopGroup(properties.getBossThreads());
        this.worker = new NioEventLoopGroup(properties.getWorkerThreads());
        this.segment = new NioEventLoopGroup(properties.getWorkerThreads());
    }


    public EventLoopGroup getBoss() {
        return boss;
    }

    public EventLoopGroup getWorker() {
        return worker;
    }

    public EventLoopGroup getSegment() {
        return segment;
    }

    public void executeRunnable(Runnable runnable) {
        this.segment.execute(runnable);
    }

    public Future<?> submitRunnable(Runnable runnable) {
        return segment.submit(runnable);
    }

    public void shutdown() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        segment.shutdownGracefully();
    }

}
