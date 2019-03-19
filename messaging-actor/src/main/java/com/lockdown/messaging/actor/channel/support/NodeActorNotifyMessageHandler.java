package com.lockdown.messaging.actor.channel.support;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.actor.command.NodeActorNotifyMessage;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.support.ChannelInboundHandlerAdapter;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeActorNotifyMessageHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        NodeCommand command = (NodeCommand) event.getParam();
        if (command instanceof NodeActorNotifyMessage) {
            logger.info("{} handle node actor notify message {}!",ctx.eventLoop().localDestination(),command);
            NodeActorNotifyMessage notifyMessage = (NodeActorNotifyMessage) command;
            Object originMessage = ((ActorChannelEventLoop)ctx.eventLoop()).actorMessageCodec().decode(notifyMessage.getContent());
            logger.info(" 推送消息 {}", JSON.toJSONString(originMessage));
            ctx.eventLoop().notifyWriteMessage(ActorChannel.type(),originMessage);
//            ctx.eventLoop().notifyWriteMessage(ActorChannel.type(),((ActorChannelEventLoop)ctx.eventLoop()).actorMessageCodec().decode(notifyMessage.getContent()));
        }else{
            ctx.fireChannelReceived(message);
        }
    }
}
