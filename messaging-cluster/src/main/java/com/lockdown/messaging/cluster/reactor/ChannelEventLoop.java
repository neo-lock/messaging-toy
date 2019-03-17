package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.support.LocalChannel;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.support.ChannelEventInvokerContext;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;

public interface ChannelEventLoop {

    void channelEvent(ChannelEvent event);

    void execute(Runnable runnable);

    void registerNodeChannel(ChannelFuture future, ServerDestination destination);

    ServerDestination localDestination();

    void start();

    void shutdown();

    void scheduleEvent(ChannelEvent event, long delay, TimeUnit unit);

    NodeChannelGroup nodeChannelGroup();

    ChannelEventInvokerContext eventInvokerContext();

    LocalChannel localChannel();

    LocalNode localNode();

    ServerContext serverContext();


}
