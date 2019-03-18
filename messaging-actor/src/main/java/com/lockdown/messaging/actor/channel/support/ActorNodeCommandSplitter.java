package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.command.NodeActorMessage;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.support.NodeCommandSplitter;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorNodeCommandSplitter extends NodeCommandSplitter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(ChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        NodeCommand command = (NodeCommand) event.getParam();
        if (command instanceof NodeActorMessage) {
            NodeActorMessage actorMessage = (NodeActorMessage) command;
            ActorDestination receiver = new ActorDestination(actorMessage.getChannelId(), actorMessage.getSource());
            ChannelEvent forward = new ChannelEvent(ActorChannel.type(), ChannelEventType.CHANNEL_READ, receiver, actorMessage);
            ctx.eventLoop().channelEvent(forward);
        } else {
            super.channelReceived(ctx, message);
        }
    }


    @Override
    public void channelClosed(ChannelContext ctx) {
        super.channelClosed(ctx);
    }
}
