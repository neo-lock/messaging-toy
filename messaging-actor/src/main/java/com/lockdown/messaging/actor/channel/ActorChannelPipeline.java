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

class ActorChannelPipeline extends AbstractChannelPipeline {


    ActorChannelPipeline(Channel channel) {
        super(channel, new HeadChannelHandler(channel), new ActorTailHandler(((ActorChannel)channel)));
    }


    private static class ActorTailHandler implements ChannelInboundHandler {

        private final ActorChannel channel;

        private ActorTailHandler(ActorChannel channel) {
            this.channel = channel;
        }

        @Override
        public void channelReceived(ChannelContext ctx, Object message) {
            if (message instanceof NodeActorMessage) {
                NodeActorMessage actorMessage = (NodeActorMessage) message;
                ActorDestination actorDestination = actorMessage.getDestination();
                channel.actor().receivedMessage(actorDestination, channel.actorMessageCodec().decode(actorMessage.getContent()));
            } else {
                channel.actor().receivedMessage(message);
            }
        }

        @Override
        public void exceptionCaught(ChannelContext ctx, Throwable throwable) {
            channel.actor().exceptionCaught(throwable);
        }

        @Override
        public void channelClosed(ChannelContext ctx) {
            channel.actor().closedEvent();
        }
    }


}
