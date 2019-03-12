package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.ExecutorService;

public interface ChannelEventLoop {

    void registerNodeChannel(ChannelFuture channel, ServerDestination destination);

    void channelEvent(ChannelEvent event);

    ExecutorService executor();

    void execute(Runnable runnable);

    NodeChannelGroup nodeChannelGroup();

    void start();

    void shutdown();

}
