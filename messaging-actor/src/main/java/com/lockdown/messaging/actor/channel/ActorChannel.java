package com.lockdown.messaging.actor.channel;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorMessageCodec;
import com.lockdown.messaging.actor.command.NodeActorMessage;
import com.lockdown.messaging.cluster.channel.ChannelPipeline;
import com.lockdown.messaging.cluster.channel.support.NodeChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ActorChannel extends NodeChannel {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ActorChannel(ChannelEventLoop eventLoop, ActorDestination destination, ChannelFuture channelFuture) {
        super(eventLoop, destination, channelFuture);
    }

    public static Enum<?> type() {
        return ChannelType.ACTOR;
    }

    @Override
    protected ChannelPipeline providerPipeline() {
        return new ActorChannelPipeline(this);
    }

    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        super.handleEvent(channelEvent);
    }

    public void writeAndFlush(ActorDestination destination, Object message, boolean autoWrite) {
        if (destination.getDestination().equals(eventLoop().localDestination())) {
            writeAndFlush(message);
        } else {
            NodeActorMessage nodeActorMessage = new NodeActorMessage();
            nodeActorMessage.setAutoWrite(autoWrite);
            nodeActorMessage.setContent(actorMessageCodec().encode(message));
            nodeActorMessage.setDestination((ActorDestination) destination());
            nodeActorMessage.setSource(destination.getDestination());
            nodeActorMessage.setChannelId(destination.getChannelId());
            ChannelEvent channelEvent = new ChannelEvent(this,NodeChannel.type(), ChannelEventType.CHANNEL_WRITE, destination.getDestination(), nodeActorMessage);
            eventLoop().channelEvent(channelEvent);
        }
    }

    public ActorMessageCodec actorMessageCodec() {
        return ((ActorChannelEventLoop) eventLoop()).actorMessageCodec();
    }

    @Override
    public Enum<?> channelType() {
        return type();
    }

    private enum ChannelType {
        ACTOR;
    }
}
