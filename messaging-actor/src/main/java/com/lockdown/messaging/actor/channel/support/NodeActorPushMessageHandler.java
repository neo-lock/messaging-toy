package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.actor.command.NodeActorPushMessage;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.support.ChannelInboundHandlerAdapter;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeActorPushMessageHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        NodeCommand command = (NodeCommand) event.getParam();
        if(command instanceof NodeActorPushMessage){
            NodeActorPushMessage pushMessage = (NodeActorPushMessage) command;
            Object origin = ((ActorChannelEventLoop)ctx.eventLoop()).actorMessageCodec().decode(((NodeActorPushMessage) command).getContent());
            ChannelEvent forward = new ChannelEvent(ActorChannel.type(), ChannelEventType.CHANNEL_WRITE,pushMessage.getDestination(),origin);
            ctx.eventLoop().channelEvent(forward);
        }else{
            ctx.fireChannelReceived(message);
        }

    }
}
