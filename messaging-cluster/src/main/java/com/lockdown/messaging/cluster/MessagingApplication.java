package com.lockdown.messaging.cluster;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class MessagingApplication {


    public static void main(String[] args) {

        MessagingNodeContext serverContext = new MessagingNodeContext(new MessagingProperties() {
            @Override
            public int getNodePort() {
                return 9092;
            }

            @Override
            public Destination masterTarget() {
                //return null;
                return new ServerDestination("192.168.56.1", 9090);
            }
        });
        serverContext.setBossGroup(new NioEventLoopGroup(1));
        EventLoopGroup worker = new NioEventLoopGroup(2);
        serverContext.setWorkerGroup(worker);
        serverContext.setSegmentGroup(worker);
        serverContext.initLocalClient();
        ClusterLocalServer localServer = new ClusterLocalServer(serverContext);
        try {
            localServer.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            serverContext.shutdownExecutor();
        }
    }


}
