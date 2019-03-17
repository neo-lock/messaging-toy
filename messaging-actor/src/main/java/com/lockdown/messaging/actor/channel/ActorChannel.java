package com.lockdown.messaging.actor.channel;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorMessageCodec;
import com.lockdown.messaging.actor.command.NodeActorMessage;
import com.lockdown.messaging.cluster.channel.support.NodeChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import io.netty.channel.ChannelFuture;



public class ActorChannel extends NodeChannel {


    public ActorChannel(ChannelEventLoop eventLoop, ActorDestination destination, ChannelFuture channelFuture) {
        super(eventLoop,destination, channelFuture);
    }


    @Override
    public void handleEvent(ChannelEvent channelEvent) {
        super.handleEvent(channelEvent);
    }

    public void writeAndFlush(ActorDestination destination,Object message){
        if(destination.getDestination().equals(eventLoop().localDestination())){
            writeAndFlush(message);
        }else{
            NodeActorMessage nodeActorMessage = new NodeActorMessage();
            nodeActorMessage.setContent(actorMessageCodec().encode(message));
            nodeActorMessage.setDestination((ActorDestination) destination());
            nodeActorMessage.setSource(destination.getDestination());
            nodeActorMessage.setChannelId(destination.getChannelId());
            ChannelEvent channelEvent = new ChannelEvent(NodeChannel.type(),ChannelEventType.CHANNEL_WRITE,destination.getDestination(),nodeActorMessage);
            eventLoop().channelEvent(channelEvent);
        }
    }

    public ActorMessageCodec actorMessageCodec(){
        return ((ActorChannelEventLoop)eventLoop()).actorMessageCodec();
    }


    public static Enum<?> type(){
        return ChannelType.ACTOR;
    }

    private enum ChannelType{
        ACTOR;
    }
}
