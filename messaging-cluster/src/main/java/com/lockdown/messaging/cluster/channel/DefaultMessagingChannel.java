package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.framwork.Destination;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ReactorContext;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.ExecutorService;


public class DefaultMessagingChannel implements MessagingChannel {


    private final ChannelHandlerContext ctx;
    private Destination destination;
    private DefaultMessagingChannelPipeline pipeline;
    private ReactorContext reactorContext;


    public DefaultMessagingChannel(ChannelHandlerContext channelHandlerContext, ExecutorService executorService, ReactorContext reactorContext) {
        this.ctx = channelHandlerContext;
        this.pipeline = new DefaultMessagingChannelPipeline(this,executorService);
        this.reactorContext = reactorContext;
    }


    @Override
    public MessagingChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public ChannelHandlerContext nettyChannelContext() {
        return ctx;
    }

    @Override
    public Destination destination() {
        return destination;
    }

    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        switch (channelEvent.getEventType()){
            case CHANNEL_CLOSE:{
                pipeline.fireChannelClosed();
                break;
            }

            default:{
                pipeline.fireChannelReceived(channelEvent.getParam());
            }
        }
    }

}
