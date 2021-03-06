package com.lockdown.messaging.actor.channel.support;

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
            NodeActorNotifyMessage notifyMessage = (NodeActorNotifyMessage) command;
            Object originMessage = ((ActorChannelEventLoop) ctx.eventLoop()).actorMessageCodec().decode(notifyMessage.getContent());
            ctx.eventLoop().notifyWriteMessage(ActorChannel.type(), originMessage);
        } else {
            ctx.fireChannelReceived(message);
        }
    }
}
