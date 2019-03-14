package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.LocalChannelContext;
import com.lockdown.messaging.cluster.channel.LocalChannelHandler;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class LocalNodeMasterHandler implements LocalChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(LocalChannelContext ctx, Object message) {
        logger.info(" message :{}",message);
        ChannelEvent event = (ChannelEvent) message;
        switch (event.getEventType()){
            case RANDOM_REGISTER:{
                try{
                    logger.info("随机注册");
                    LocalNode node = ctx.pipeline().channel().localNode();
                    Channel channel = ctx.eventLoop().nodeChannelGroup().randomNodeChannel();
                    ChannelEvent registerEvent = new ChannelEvent(ChannelEventType.CHANNEL_WRITE,channel.destination(),new NodeRegister(node.destination()));
                    ctx.eventLoop().channelEvent(registerEvent);
                }catch (Throwable e){
                    e.printStackTrace();
                    ctx.eventLoop().scheduleEvent(event,5,TimeUnit.SECONDS);
                }
                break;
            }
            case REGISTER_MASTER:{
                try{
                    logger.info("注册master {}",event.getParam());
                    LocalNode node = ctx.pipeline().channel().localNode();
                    Channel channel = ctx.eventLoop().nodeChannelGroup().getMasterNodeChannel((Destination) event.getParam());
                    ChannelEvent registerEvent = new ChannelEvent(ChannelEventType.CHANNEL_WRITE,channel.destination(),new NodeRegister(node.destination()));
                    ctx.eventLoop().channelEvent(registerEvent);
                }catch (Throwable ex){
                    ex.printStackTrace();
                    ctx.eventLoop().scheduleEvent(event,5,TimeUnit.SECONDS);
                }
                break;
            }
            default:{
                ctx.fireChannelReceived(message);
            }
        }

    }
}
