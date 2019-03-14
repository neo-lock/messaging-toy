package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;

public interface ChannelEventLoop {

    void registerNodeChannelGroup(NodeChannelFactoryGroup nodeChannelFactoryGroup);

    void registerNodeChannel(ChannelFuture channel, ServerDestination destination);

    void channelEvent(ChannelEvent event);

    void execute(Runnable runnable);

    NodeChannelGroup nodeChannelGroup();

    ServerDestination localDestination();

    void start();

    void shutdown();

    void scheduleEvent(ChannelEvent event, long delay, TimeUnit unit);

}
