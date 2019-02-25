package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.node.LocalServerNode;
import com.lockdown.messaging.cluster.node.ServerNodeEventHandler;
import com.lockdown.messaging.cluster.node.ServerNodeEventListener;
import com.lockdown.messaging.cluster.utils.IPUtils;
import com.sun.istack.internal.NotNull;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

public class MessagingNodeContext{

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Destination localDestination;

    private MessagingProperties properties;

    @NotNull
    private EventLoopGroup bossGroup;
    @NotNull
    private EventLoopGroup workerGroup;
    @NotNull
    private ExecutorService segmentGroup;

    private LocalServerNode localServerNode;

    private LocalServer localServer;


    private ServerNodeEventHandler eventHandler = new ServerNodeEventHandler();

    private Set<EventLoopGroup> clientGroups = new HashSet<>();

    public EventLoopGroup createEventLoopGroup(int thread){
        EventLoopGroup loopGroup = new NioEventLoopGroup(thread);
        clientGroups.add(loopGroup);
        return loopGroup;
    }

    public LocalClient newLocalClient(){
        return new ClusterLocalClient(this);
    }


    public MessagingNodeContext(MessagingProperties properties) throws UnknownHostException {
        this.properties = properties;
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(),properties.getNodePort());
    }


    public Destination getLocalDestination() {
        return this.localDestination;
    }

    public ServerNodeEventHandler getEventHandler() {
        return eventHandler;
    }

    public void registerEventHandler(ServerNodeEventListener eventListener){
        this.eventHandler.setEventListener(eventListener);
    }


    public LocalServer getLocalServer() {
        return localServer;
    }

    public void setLocalServer(LocalServer localServer) {
        this.localServer = localServer;
    }

    public LocalServerNode getLocalServerNode() {
        return localServerNode;
    }

    public void setLocalServerNode(LocalServerNode localServerNode) {
        this.localServerNode = localServerNode;
    }

    public MessagingProperties getProperties() {
        return properties;
    }

    public void setProperties(MessagingProperties properties) {
        this.properties = properties;
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

    public ExecutorService getSegmentGroup() {
        return segmentGroup;
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


    public Destination getClusterDestination() {
        return properties.masterTarget();
    }
}
