package com.lockdown.messaging.cluster.reactor.support;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.NodeChannelFactoryGroup;
import com.lockdown.messaging.cluster.reactor.NodeChannelGroup;
import io.netty.channel.ChannelFuture;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.*;

public class DisruptorChannelEventLoop implements ChannelEventLoop,EventHandler<ChannelEvent> {


    private Disruptor<ChannelEvent> disruptor;
    private RingBuffer<ChannelEvent> ringBuffer;
    private NodeChannelFactoryGroup nodeChannelGroup;
    private ServerContext serverContext;
    private ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors()*2,10,TimeUnit.MINUTES,new LinkedBlockingQueue<>(128));


    public DisruptorChannelEventLoop(ServerContext serverContext){
        this.serverContext = serverContext;
        this.disruptor = new Disruptor<>(new ChannelEventFactory(), 1024 * 1024, Executors.defaultThreadFactory(), ProducerType.MULTI, new YieldingWaitStrategy());
        this.disruptor.handleEventsWith(this);
    }



    @Override
    public void registerNodeChannelGroup(NodeChannelFactoryGroup nodeChannelFactoryGroup) {
        this.nodeChannelGroup = nodeChannelFactoryGroup;
    }

    @Override
    public void registerNodeChannel(ChannelFuture channel, ServerDestination destination) {
        nodeChannelGroup.newInstance(channel,destination);
    }

    @Override
    public void channelEvent(ChannelEvent event) {
        long sequence = ringBuffer.next();
        try{
            ChannelEvent setup = ringBuffer.get(sequence);
            setup.setDestination(event.getDestination());
            setup.setEventType(event.getEventType());
            setup.setParam(event.getParam());
        }finally {
            ringBuffer.publish(sequence);
        }
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
        ringBuffer = disruptor.start();
    }

    @Override
    public void shutdown() {
        disruptor.shutdown();
    }

    @Override
    public void scheduleEvent(ChannelEvent event, long delay, TimeUnit unit) {
        serverContext.runtimeEnvironment().newTimeout(new ChannelEventTimer(event),delay,unit);
    }

    @Override
    public void onEvent(ChannelEvent channelEvent, long l, boolean b) throws Exception {
        Channel channel = nodeChannelGroup().getNodeChannel(channelEvent.getDestination());
        channel.handleEvent(channelEvent);
    }

    private class ChannelEventFactory implements EventFactory<ChannelEvent>{

        @Override
        public ChannelEvent newInstance() {
            return new ChannelEvent();
        }
    }

    private class ChannelEventTimer implements TimerTask{

        private final ChannelEvent event;

        private ChannelEventTimer(ChannelEvent event) {
            this.event = event;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            channelEvent(event);
        }
    }

}
