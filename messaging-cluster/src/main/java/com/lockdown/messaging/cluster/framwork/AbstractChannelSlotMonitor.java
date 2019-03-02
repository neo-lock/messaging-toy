package com.lockdown.messaging.cluster.framwork;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.exception.MessagingDestinationNotFoundException;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractChannelSlotMonitor <T extends ChannelSlot<D,M>,D extends Destination,B extends ChannelBeanFactory<T,D>,M>
        implements ChannelSlotMonitor<T,D,M>,ChannelBeanFactory<T,D>,MessageTrigger<T, M> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Map<D, T> destinationContext = new ConcurrentHashMap<>();
    protected MessageAcceptor<T, M> messageAcceptor;
    protected final  B beanFactory;

    public AbstractChannelSlotMonitor(B beanFactory) {
        this.beanFactory = beanFactory;
    }


    @Override
    public T findByDestination(D destination) {
        Objects.requireNonNull(destination);
        if (destinationContext.containsKey(destination)) {
            throw new MessagingDestinationNotFoundException(destination);
        }
        return destinationContext.get(destination);
    }

    @Override
    public Collection<T> AllDestination() {
        return destinationContext.values();
    }

    @Override
    public void printNodes() {
        logger.debug("Current Destination : {}", JSON.toJSONString(destinationContext.keySet()));
    }

    @Override
    public void shutdown() {
        destinationContext.values().forEach(ChannelSlot::close);
    }

    @Override
    public void inactive(T slot) {
        Objects.requireNonNull(slot);
        T old = destinationContext.remove(slot.destination());
        if(Objects.nonNull(old)){
            logger.info(" ========{} inactive  关闭===============",slot.destination());
            old.close();
        }
    }


    @Override
    public void exceptionCaught(T slot, Throwable cause) {
        //inactive(slot);
        //ignore
        logger.warn(" {} 出现异常 {}",slot.destination(),cause.getMessage());
    }

    @Override
    public void registerAcceptor(MessageAcceptor<T, M> acceptor) {
        this.messageAcceptor = acceptor;
    }


    @Override
    public void messageTriggered(T channelSlot, M message) {
        this.messageAcceptor.acceptedMessage(channelSlot,message);
    }


    @Override
    public T getInstance(ChannelFuture channelFuture, D destination) {
        T bean = beanFactory.getInstance(channelFuture, destination);
        if(Objects.nonNull(bean)){
            destinationContext.putIfAbsent(destination,bean);
        }
        return bean;
    }

}
