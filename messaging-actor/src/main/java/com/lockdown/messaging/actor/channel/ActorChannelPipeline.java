package com.lockdown.messaging.actor.channel;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.command.NodeActorMessage;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.ChannelContext;
import com.lockdown.messaging.cluster.channel.ChannelInboundHandler;
import com.lockdown.messaging.cluster.channel.support.AbstractChannelPipeline;
import com.lockdown.messaging.cluster.channel.support.HeadChannelHandler;

public class ActorChannelPipeline extends AbstractChannelPipeline {


    ActorChannelPipeline(Channel channel) {
        super(channel, new HeadChannelHandler(channel), new ActorTailHandler((ActorChannel) channel));
    }


    private static class ActorTailHandler implements ChannelInboundHandler {

        private final ActorChannel actorChannel;
        private Actor actor;

        private ActorTailHandler(ActorChannel actorChannel) {
            this.actorChannel = actorChannel;
            this.actor =  ((ActorServerContext)this.actorChannel.eventLoop().serverContext()).actorFactory().newInstance(actorChannel);
        }

        @Override
        public void channelReceived(ChannelContext ctx, Object message) {

            if (message instanceof NodeActorMessage) {
                NodeActorMessage actorMessage = (NodeActorMessage) message;
                ActorDestination actorDestination = actorMessage.getDestination();
                actor.receivedMessage(actorDestination, actorChannel.actorMessageCodec().decode(actorMessage.getContent()));
            } else {
                actor.receivedMessage(message);
            }
        }

        @Override
        public void exceptionCaught(ChannelContext ctx, Throwable throwable) {
            actor.exceptionCaught(throwable);
        }

        @Override
        public void channelClosed(ChannelContext ctx) {
            actor.closedEvent();
        }
    }


}
