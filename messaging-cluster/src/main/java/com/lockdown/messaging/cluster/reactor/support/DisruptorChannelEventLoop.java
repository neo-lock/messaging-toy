package com.lockdown.messaging.cluster.reactor.support;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelEventLoopInitializer;
import com.lockdown.messaging.cluster.channel.support.LocalChannel;
import com.lockdown.messaging.cluster.node.ClusterLocalNode;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.NodeChannelGroup;
import io.netty.channel.ChannelFuture;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class DisruptorChannelEventLoop implements ChannelEventLoop, EventHandler<ChannelEvent> {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private Disruptor<ChannelEvent> disruptor;
    private RingBuffer<ChannelEvent> ringBuffer;
    private NodeChannelGroup nodeChannelGroup;
    private ServerContext serverContext;
    private LocalChannel localChannel;
    private LocalNode localNode;
    private ServerDestination localDestination;
    private ChannelEventInvokerContext eventInvokerContext;
    private ChannelEventLoopInitializer<ChannelEventLoop> eventLoopInitializer;
    private ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(128));


    public DisruptorChannelEventLoop(ServerContext serverContext, ChannelEventLoopInitializer<ChannelEventLoop> eventLoopInitializer) {
        this.serverContext = serverContext;
        this.disruptor = new Disruptor<>(new ChannelEventFactory(), 1024 * 1024, Executors.defaultThreadFactory(), ProducerType.MULTI, new BlockingWaitStrategy());
        this.disruptor.handleEventsWith(this);
        this.localDestination = serverContext.localDestination();
        this.localNode = new ClusterLocalNode(this.localDestination,this);
        this.localChannel = new LocalChannelFactory(this).newInstance(null, this.localDestination);
        this.nodeChannelGroup = new DefaultChannelGroup(serverContext, this);
        this.eventInvokerContext = new DefaultChannelEventInvokerContext();
        this.eventLoopInitializer = eventLoopInitializer;
        this.initChannelEventInvoker();
    }

    private void initChannelEventInvoker() {
        this.eventLoopInitializer.initialize(this);
    }


    @Override
    public void channelEvent(ChannelEvent event) {
        long sequence = ringBuffer.next();
        try {
            ChannelEvent setup = ringBuffer.get(sequence);
            setup.setChannelType(event.getChannelType());
            setup.setDestination(event.getDestination());
            setup.setEventType(event.getEventType());
            setup.setParam(event.getParam());
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    @Override
    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public void registerNodeChannel(ChannelFuture future, ServerDestination destination) {
        nodeChannelGroup.newInstance(future, destination);
    }


    @Override
    public ServerDestination localDestination() {
        return this.localDestination;
    }

    @Override
    public void start() {
        ringBuffer = disruptor.start();
    }

    @Override
    public void shutdown() {
        disruptor.shutdown();
    }

    @Override
    public void scheduleEvent(ChannelEvent event, long delay, TimeUnit unit) {
        serverContext.runtimeEnvironment().newTimeout(new ChannelEventTimer(event), delay, unit);
    }

    @Override
    public NodeChannelGroup nodeChannelGroup() {
        return nodeChannelGroup;
    }

    @Override
    public ChannelEventInvokerContext eventInvokerContext() {
        return eventInvokerContext;
    }

    @Override
    public LocalChannel localChannel() {
        return localChannel;
    }

    @Override
    public LocalNode localNode() {
        return this.localNode;
    }

    @Override
    public void onEvent(ChannelEvent channelEvent, long l, boolean b) throws Exception {
        this.eventInvokerContext.handleEvent(channelEvent);
    }


    private class ChannelEventFactory implements EventFactory<ChannelEvent> {

        @Override
        public ChannelEvent newInstance() {
            return new ChannelEvent();
        }
    }

    private class ChannelEventTimer implements TimerTask {

        private final ChannelEvent event;

        private ChannelEventTimer(ChannelEvent event) {
            this.event = event;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            channelEvent(event);
        }
    }

    public class DefaultChannelEventInvokerContext implements ChannelEventInvokerContext {
        private Map<Enum<?>, ChannelTypeEventInvoker> invokerMap = new HashMap<>();

        @Override
        public void handleEvent(ChannelEvent event) {
            invokerMap.get(event.getChannelType()).handleEvent(event);
        }

        @Override
        public void registerEventInvoker(ChannelTypeEventInvoker eventInvoker) {
            invokerMap.putIfAbsent(eventInvoker.supported(), eventInvoker);
        }
    }

}
