package com.lockdown.messaging.cluster.reactor.support;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.support.DefaultLocalChannelFactory;
import com.lockdown.messaging.cluster.node.ClusterLocalNode;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.*;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultChannelEventLoop implements ChannelEventLoop,Runnable {


    private final Selector selector;
    private final ServerContext serverContext;
    private NodeChannelFactoryGroup nodeChannelGroup;
    //########################################################
    //请求队列无限制，后面需要更改!!!!
    private ScheduledExecutorService executorService;
    //########################################################

    private volatile boolean running = true;
    private Logger logger = LoggerFactory.getLogger(getClass());


    public DefaultChannelEventLoop(ServerContext serverContext) {
        this.selector = new DefaultSelector();
        this.serverContext = serverContext;
        this.executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    }

    public void setNodeChannelGroup(NodeChannelFactoryGroup nodeChannelGroup) {
        this.nodeChannelGroup = nodeChannelGroup;
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
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public NodeChannelGroup nodeChannelGroup() {
        return nodeChannelGroup;
    }

    @Override
    public ServerDestination localDestination() {
        return serverContext.localDestination();
    }


    @Override
    public void start() {
        executorService.submit(this);
    }

    @Override
    public void shutdown() {
        this.running = false;
        this.executorService.shutdown();
    }

    @Override
    public void scheduleEvent(ChannelEvent event, long delay, TimeUnit unit) {
        executorService.schedule(new EventTask(event),delay,unit);
    }

    @Override
    public void run() {
        while (running) {
            List<ChannelEvent> eventList = selector.select();
            if (eventList.isEmpty()) {
                continue;
            }
            eventList.forEach(event -> {
                execute(() -> {
                    Channel channel = nodeChannelGroup.getNodeChannel(event.getDestination());
                    channel.handleEvent(event);
                });
            });
        }
    }




    private class EventTask implements Runnable {
        private final ChannelEvent event;

        private EventTask(ChannelEvent event) {
            this.event = event;
        }

        @Override
        public void run(){
            DefaultChannelEventLoop.this.channelEvent(event);
        }
    }

}
