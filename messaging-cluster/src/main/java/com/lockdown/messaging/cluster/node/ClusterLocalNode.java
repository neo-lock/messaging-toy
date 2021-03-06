package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.support.LocalChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 状态
 * <p>
 * 最新状态
 * <p>
 * 有了跟班的状态
 */
public class ClusterLocalNode implements LocalNode {


    private final ServerDestination localDestination;
    private final ChannelEventLoop eventLoop;
    private final Object lock = new Object();
    private Logger logger = LoggerFactory.getLogger(getClass());
    private volatile ServerDestination monitor = null;
    private volatile ServerDestination attached = null;


    public ClusterLocalNode(ServerDestination localDestination, ChannelEventLoop eventLoop) {
        this.localDestination = localDestination;
        this.eventLoop = eventLoop;
    }


    @Override
    public void registerToCluster(ServerDestination destination) {
        ChannelEvent channelEvent = new ChannelEvent(LocalChannel.type(), ChannelEventType.REGISTER_MASTER, localDestination, destination);
        eventLoop.channelEvent(channelEvent);
    }


    @Override
    public boolean isMonitored() {
        return Objects.nonNull(monitor);
    }


    @Override
    public void monitor(ServerDestination destination) {
        synchronized (lock) {
            monitor = destination;
        }

    }

    @Override
    public void attachTo(ServerDestination destination) {
        synchronized (lock) {
            attached = destination;
        }
    }

    @Override
    public boolean isAttached() {
        return Objects.nonNull(attached);
    }

    @Override
    public synchronized boolean monitorCompareAndSet(ServerDestination old, ServerDestination update) {
        boolean result = false;
        synchronized (lock) {
            if (Objects.isNull(old)) {
                if (monitor == null) {
                    monitor = update;
                    result = true;
                }
            } else {
                if (old.equals(monitor)) {
                    monitor = update;
                    result = true;
                }
            }
        }
        return result;
    }

    @Override
    public boolean attachedCompareAndSet(ServerDestination old, ServerDestination update) {
        boolean result = false;
        synchronized (lock) {
            if (Objects.isNull(old)) {
                if (attached == null) {
                    attached = update;
                    result = true;
                }
            } else {
                if (old.equals(attached)) {
                    attached = update;
                    result = true;
                }
            }
        }
        return result;
    }


    @Override
    public void registerRandomNode() {
        logger.info("开始随机注册节点");
        ChannelEvent channelEvent = new ChannelEvent(LocalChannel.type(), ChannelEventType.RANDOM_REGISTER, localDestination);
        eventLoop.channelEvent(channelEvent);
    }

    @Override
    public void printNodes() {
        logger.info("Local[{}],Monitor[{}],Attached[{}]", localDestination, monitor, attached);
    }

    @Override
    public ServerDestination destination() {
        return localDestination;
    }


}
