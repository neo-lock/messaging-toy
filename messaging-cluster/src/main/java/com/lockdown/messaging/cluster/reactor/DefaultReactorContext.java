package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.channel.MessagingChannelFactory;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

public class DefaultReactorContext implements ReactorContext {

    private Selector selector = new DefaultSelector();

    private Dispatcher dispatcher;

    public DefaultReactorContext(NodeChannelGroup channelGroup, ExecutorService executorService){
        this.dispatcher = new DefaultDispatcher(selector,channelGroup,executorService);
    }


    @Override
    public void registerChannel(ChannelHandlerContext ctx) {

    }

    @Override
    public void channelEvent(ChannelEvent event) {
        selector.addEvent(event);
    }

    @Override
    public MessagingChannel newInstance(ChannelHandlerContext ctx) {
        return null;
    }
}
