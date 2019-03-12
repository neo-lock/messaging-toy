package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.reactor.*;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class DefaultChannelEventLoop implements ChannelEventLoop, Runnable {


    private final Selector selector;
    private NodeChannelFactoryGroup nodeChannelGroup;
    private ExecutorService executorService;
    private volatile boolean running = true;
    private MessagingChannel localChannel;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultChannelEventLoop(ExecutorService executorService) {
        this.selector = new DefaultSelector();
        this.executorService = executorService;
    }

    public void setLocalChannel(MessagingChannel localChannel) {
        this.localChannel = localChannel;
    }

    public void setNodeChannelGroup(NodeChannelFactoryGroup nodeChannelGroup) {
        this.nodeChannelGroup = nodeChannelGroup;
    }

    @Override
    public void run() {
        while (running) {
            List<ChannelEvent> eventList = selector.select();
            eventList.forEach(event -> {
                logger.debug("处理节点事件{}",event);
                if(event.getEventType() == ChannelEventType.NODE){
                    executorService.execute(() -> localChannel.handleEvent(event));
                }else{
                    executorService.execute(() -> nodeChannelGroup.getNodeChannel(event.getDestination()).handleEvent(event));
                }
            });
        }
    }

    @Override
    public void registerNodeChannel(ChannelFuture channel, ServerDestination destination) {
        nodeChannelGroup.newInstance(channel, destination);
    }

    @Override
    public void channelEvent(ChannelEvent event) {
        selector.addEvent(event);
    }

    @Override
    public ExecutorService executor() {
        return executorService;
    }

    @Override
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public NodeChannelGroup nodeChannelGroup() {
        return nodeChannelGroup;
    }

    @Override
    public void start() {
        executorService.submit(this);
    }

    @Override
    public void shutdown() {
        this.running = false;
    }

}
