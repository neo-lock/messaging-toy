package com.lockdown.messaging.cluster.channel.support;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.MessagingChannelPipeline;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRemoteNodeChannel extends AbstractMessagingChannel implements RemoteNodeChannel {


    private final ChannelFuture channelFuture;
    private final ServerDestination destination;
    private final DefaultMessagingChannelPipeline pipeline;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    DefaultRemoteNodeChannel(ChannelEventLoop eventLoop, ChannelFuture channel, ServerDestination destination) {
        super(eventLoop);
        this.channelFuture = channel;
        this.destination = destination;
        this.pipeline = new DefaultMessagingChannelPipeline(this);
    }


    @Override
    protected void processEvent(ChannelEvent event) {
        pipeline.fireChannelReceived(event);
    }

    @Override
    public MessagingChannelPipeline pipeline() {
        return pipeline;
    }


    @Override
    public void writeAndFlush(Object obj) {
        logger.debug("发送消息{}{}",obj.getClass(),JSON.toJSONString(obj));
        logger.debug(" 当前channel是否可写 {}",channelFuture.channel().isWritable());
        channelFuture.channel().writeAndFlush(obj);

    }


    @Override
    public void close() {
        channelFuture.channel().close();
    }

    @Override
    public ServerDestination destination() {
        return destination;
    }

}
