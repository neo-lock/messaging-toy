package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandlerAdapter;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class LocalNodeMasterHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        switch (event.getEventType()) {
            case RANDOM_REGISTER: {
                try {
                    logger.debug("随机注册");
                    Channel channel = ctx.eventLoop().nodeChannelGroup().randomNodeChannel();
                    ChannelEvent registerEvent = new ChannelEvent(ctx.pipeline().channel(),NodeChannel.type(), ChannelEventType.CHANNEL_WRITE, channel.destination(), new NodeRegister(ctx.eventLoop().localDestination()));
                    ctx.eventLoop().channelEvent(registerEvent);
                } catch (Throwable e) {
                    logger.warn("随机注册失败! 准备重新注册!");
                    ctx.eventLoop().scheduleEvent(event, 5, TimeUnit.SECONDS);
                }
                break;
            }
            case REGISTER_MASTER: {
                try {
                    logger.debug("注册Master {}", event.getParam());
                    Channel channel = ctx.eventLoop().nodeChannelGroup().connectOnNotExists((Destination) event.getParam());
                    ChannelEvent registerEvent = new ChannelEvent(ctx.pipeline().channel(),NodeChannel.type(), ChannelEventType.CHANNEL_WRITE, channel.destination(), new NodeRegister(ctx.eventLoop().localDestination()));
                    ctx.eventLoop().channelEvent(registerEvent);
                } catch (Throwable ex) {
                    logger.warn("注册Master {} 失败! 准备重新注册!",event.getParam());
                    ctx.eventLoop().scheduleEvent(event, 5, TimeUnit.SECONDS);
                }
                break;
            }
            default: {
                ctx.fireChannelReceived(message);
            }
        }
    }

}
