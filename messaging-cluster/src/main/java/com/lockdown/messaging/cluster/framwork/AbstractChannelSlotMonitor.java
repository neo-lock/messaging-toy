package com.lockdown.messaging.cluster.framwork;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.exception.MessagingDestinationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractChannelSlotMonitor <T extends ChannelSlot<D,M>,D extends Destination,M>
        implements ChannelSlotMonitor<T,D>{



    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Map<D, T> destinationContext = new ConcurrentHashMap<>();


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
        logger.warn(" {} 出现异常 {}",slot.destination(),cause.getMessage());
    }


}
