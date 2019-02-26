package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.channel.nio.NioEventLoopGroup;

public class MessagingApplication {

    private MessagingApplication(MessagingProperties properties) {
        try {
            new MessagingNodeContext(properties)
                    .setBossGroup(new NioEventLoopGroup(properties.getBossThreads()))
                    .setWorkerGroup(new NioEventLoopGroup(properties.getWorkerThreads()))
                    .setSegmentGroup(null)
                    .start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void run(MessagingProperties properties, String[] args) {
        new MessagingApplication(properties);
    }

    public static void main(String[] args) {
        MessagingApplication.run(new MessagingProperties() {
            @Override
            public int getNodePort() {
                return 9093;
            }

            @Override
            public ServerDestination masterTarget() {
                return new ServerDestination(IPUtils.getLocalIP(), 9090);
            }

            @Override
            public int getBossThreads() {
                return 1;
            }

            @Override
            public int getWorkerThreads() {
                return 4;
            }

            @Override
            public boolean nodeMonitorEnable() {
                return true;
            }

            @Override
            public long nodeMonitorSeconds() {
                return 10;
            }
        }, args);
    }


}
