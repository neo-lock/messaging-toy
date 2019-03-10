package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.reactor.ReactorContext;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

public class DefaultMessagingChannelFactory implements MessagingChannelFactory {

    private ExecutorService executorService;
    private ReactorContext reactorContext;
    private MessagingChannelInitializer channelInitializer;


    @Override
    public MessagingChannel newInstance(ChannelHandlerContext ctx) {
        DefaultMessagingChannel messagingChannel = new DefaultMessagingChannel(ctx,executorService,reactorContext);
        channelInitializer.initialize(messagingChannel);
        return messagingChannel;
    }



}
