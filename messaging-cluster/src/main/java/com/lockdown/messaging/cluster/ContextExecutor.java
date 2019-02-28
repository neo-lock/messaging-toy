package com.lockdown.messaging.cluster;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.ExecutorService;

public class ContextExecutor {


    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ExecutorService segment;

    public ContextExecutor(ServerProperties properties) {
        this.boss = new NioEventLoopGroup(properties.getBossThreads());
        this.worker = new NioEventLoopGroup(properties.getWorkerThreads());
        this.segment = this.worker;
    }


    public EventLoopGroup getBoss() {
        return boss;
    }

    public EventLoopGroup getWorker() {
        return worker;
    }

    public ExecutorService getSegment() {
        return segment;
    }

    public void shutdown() {
        boss.shutdownGracefully();
        worker.shutdownGracefully();
        if (!segment.isShutdown()) {
            segment.shutdown();
        }
    }

}
