package com.lockdown.messaging.cluster.reactor.support;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.ChannelEventLoopInitializer;
import com.lockdown.messaging.cluster.channel.support.LocalChannel;
import com.lockdown.messaging.cluster.node.ClusterLocalNode;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.*;
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
        logger.info("使用DisruptorEventLoop");
        this.serverContext = serverContext;
        this.disruptor = new Disruptor<>(new ChannelEventFactory(), 1024 * 1024, Executors.defaultThreadFactory(), ProducerType.MULTI, new BlockingWaitStrategy());
        this.disruptor.handleEventsWith(this);
        this.localDestination = serverContext.localDestination();
        this.localNode = new ClusterLocalNode(this.localDestination, this);
        this.localChannel = new LocalChannelFactory(this).newInstance(null, this.localDestination);
        logger.info("使用 {} 本地channel", this.localChannel.getClass());
        this.nodeChannelGroup = new DefaultChannelGroup(serverContext, this);
        logger.info("使用 {} NodeChannelGroup", this.nodeChannelGroup.getClass());
        this.eventInvokerContext = new DefaultChannelEventInvokerContext();
        logger.info("使用 {} EventInvokerContext", this.eventInvokerContext.getClass());
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
            setup.setEventSource(event.getEventSource());
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
        logger.info("当前ChannelGroup {}", nodeChannelGroup.getClass());
        nodeChannelGroup.printNodes();
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
        logger.info(" disruptor shutdown !");
        executorService.shutdown();
        logger.info(" executor service shutdown !");
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
    public ServerContext serverContext() {
        return serverContext;
    }

    @Override
    public void notifyWriteMessage(Enum<?> channelType, Object message, Destination... ignores) {
        notifyWriteMessage(channelType, message, false, ignores);
    }

    @Override
    public void notifyWriteMessage(Enum<?> channelType, Object message, boolean multiple, Destination... ignores) {
        logger.info("{} EventLoop Notify Message {} to channel {}", localDestination, JSON.toJSONString(message), channelType);
        ChannelNotifyEvent notifyEvent = new ChannelNotifyEvent(message);
        if (null != ignores && ignores.length > 0) {
            notifyEvent.addIgnores(ignores);
        }
        ChannelEvent event = new ChannelEvent(channelType, ChannelEventType.CHANNEL_NOTIFY, notifyEvent);
        channelEvent(event);
    }

    @Override
    public void onEvent(ChannelEvent channelEvent, long l, boolean b) throws Exception {
        eventInvokerContext.handleEvent(channelEvent);
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
            try {
                invokerMap.get(event.getChannelType()).handleEvent(event);
            } catch (Throwable e) {
                e.printStackTrace();
                if (null != event.getEventSource()) {
                    ChannelEvent exception = new ChannelEvent();
                    exception.setChannelType(event.getEventSource().channelType());
                    exception.setDestination(event.getEventSource().destination());
                    exception.setEventType(ChannelEventType.EXCEPTION);
                    exception.setParam(e);
                    logger.info(" 添加异常处理 {}", exception);
                    channelEvent(exception);
                } else {
                    logger.warn("当前事件 {} 发生异常! {}", event, e.getMessage());
                }

            }


        }

        @Override
        public void registerEventInvoker(ChannelTypeEventInvoker eventInvoker) {
            invokerMap.putIfAbsent(eventInvoker.supported(), eventInvoker);
        }
    }

}
