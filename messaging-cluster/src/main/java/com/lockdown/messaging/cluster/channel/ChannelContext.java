package com.lockdown.messaging.cluster.channel;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public interface ChannelContext extends ChannelInboundInvoker, ChannelOutboundInvoker, ChannelInvoker {


    ChannelHandler handler();

    ChannelPipeline pipeline();

    ChannelEventLoop eventLoop();

}
