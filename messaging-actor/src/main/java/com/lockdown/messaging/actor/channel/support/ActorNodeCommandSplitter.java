package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.command.NodeActorMessage;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.support.NodeCommandSplitter;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;

public class ActorNodeCommandSplitter extends NodeCommandSplitter {


    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        if (message instanceof NodeActorMessage) {
            NodeActorMessage actorMessage = (NodeActorMessage) message;
            ActorDestination receiver = new ActorDestination(actorMessage.getChannelId(), actorMessage.getSource());
            ChannelEvent event = new ChannelEvent(ActorChannel.type(), ChannelEventType.CHANNEL_READ, receiver, actorMessage);
            ctx.eventLoop().channelEvent(event);
        } else {
            super.channelReceived(ctx, message);
        }
    }


    @Override
    public void channelClosed(ChannelContext ctx) {
        super.channelClosed(ctx);
    }
}
