package com.lockdown.messaging.cluster;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class ContextExecutor {


    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ContextExecutor(ServerProperties properties) {
        this.boss = new NioEventLoopGroup(properties.getBossThreads());
        this.worker = new NioEventLoopGroup(properties.getWorkerThreads());
    }


    public EventLoopGroup getBoss() {
        return boss;
    }

    public EventLoopGroup getWorker() {
        return worker;
    }

    public void shutdown() {

        boss.shutdownGracefully();
        logger.info(" boss event loop shut down !");
        worker.shutdownGracefully();
        logger.info(" worker event loop shut down !");
    }

}
