package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.node.ServerNodeEventHandler;
import com.lockdown.messaging.cluster.node.ServerNodeEventListener;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class MessagingNodeContext {


    private Destination localDestination;
    private MessagingProperties properties;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ExecutorService segmentGroup;
    private ServerNodeEventHandler eventHandler = new ServerNodeEventHandler();
    private Set<EventLoopGroup> clientGroups = new HashSet<>();

    public MessagingNodeContext(MessagingProperties properties) {
        this.properties = properties;
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
    }

    public EventLoopGroup createEventLoopGroup(int thread) {
        EventLoopGroup loopGroup = new NioEventLoopGroup(thread);
        clientGroups.add(loopGroup);
        return loopGroup;
    }

    public LocalClient newLocalClient() {
        return new ClusterLocalClient(this);
    }

    public Destination getLocalDestination() {
        return this.localDestination;
    }

    public ServerNodeEventHandler getEventHandler() {
        return eventHandler;
    }

    public void registerEventHandler(ServerNodeEventListener eventListener) {
        this.eventHandler.setEventListener(eventListener);
    }


    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }


    public void setSegmentGroup(ExecutorService segmentGroup) {
        this.segmentGroup = segmentGroup;
    }

    public void shutdownExecutor() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        if (!segmentGroup.isShutdown()) {
            segmentGroup.shutdown();
        }
        clientGroups.forEach(EventExecutorGroup::shutdownGracefully);
    }


    public MessagingProperties getProperties() {
        return properties;
    }

    public Destination getClusterDestination() {
        return properties.masterTarget();
    }
}
