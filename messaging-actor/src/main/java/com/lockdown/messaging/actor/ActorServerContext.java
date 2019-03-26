package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.actor.channel.support.ActorChannelEventLoopInitializer;
import com.lockdown.messaging.actor.channel.support.ActorDisruptorChannelEventLoop;
import com.lockdown.messaging.actor.channel.support.ActorRemoteNodeChannelInitializer;
import com.lockdown.messaging.actor.command.*;
import com.lockdown.messaging.cluster.AbstractServerContext;
import com.lockdown.messaging.cluster.channel.support.NodeChannel;
import com.lockdown.messaging.cluster.command.CommandCodecHandler;
import com.lockdown.messaging.cluster.command.NodeCommandCodecHandler;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import com.lockdown.messaging.cluster.reactor.support.NodeChannelInitializer;

import java.util.Objects;

public class ActorServerContext extends AbstractServerContext<ActorProperties> {

    private ActorMessageCodec actorMessageCodec;
    private Class<?> actorClass;
    private ActorFactory actorFactory;

    public ActorServerContext(ActorProperties properties) {
        super(properties);
    }

    public void setActorMessageCodec(ActorMessageCodec actorMessageCodec) {
        this.actorMessageCodec = actorMessageCodec;
    }

    public void setActorFactory(ActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    @Override
    protected ChannelEventLoop initEventLoop() {
        return new ActorDisruptorChannelEventLoop(this, new ActorChannelEventLoopInitializer());
    }

    public ActorFactory actorFactory() {
        return this.actorFactory;
    }

    @Override
    protected void init() {
        super.init();
        this.initContext();
    }

    private void initContext() {
        try {
            this.initActorMessageCodec();
            this.checkActorClass();
            this.initActorFactory();
        } catch (Throwable t) {
            throw new MessagingException(t.getMessage());
        }
    }

    private void initActorFactory() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ActorProperties properties = getProperties();
        if (Objects.isNull(properties.getActorFactoryClassName())) {
            return;
        }
        Class<?> clazz = Class.forName(properties.getActorFactoryClassName());
        this.actorFactory = (ActorFactory) clazz.newInstance();
    }

    private void initActorMessageCodec() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (Objects.isNull(getProperties().getActorCodecClassName())) {
            return;
        }
        Class<?> codecClass = Class.forName(getProperties().getActorCodecClassName());
        this.actorMessageCodec = (ActorMessageCodec) codecClass.newInstance();
    }

    public Class<?> actorClass() {
        return actorClass;
    }

    public ActorMessageCodec actorMessageCodec() {
        return this.actorMessageCodec;
    }


    private void checkActorClass() throws ClassNotFoundException {
        if (Objects.isNull(getProperties().getActorClassName())) {
            throw new MessagingException("required ActorClassName !");
        }
        this.actorClass = Class.forName(getProperties().getActorClassName());
    }


    @Override
    public NodeChannelInitializer nodeChannelInitializer() {
        return new ActorRemoteNodeChannelInitializer();
    }

    @Override
    public CommandCodecHandler codecHandler() {
        NodeCommandCodecHandler handler = (NodeCommandCodecHandler) super.codecHandler();
        handler.registerCodec(new CommandActorMessageCodec());
        handler.registerCodec(new CommandActorNotifyMessageCodec());
        handler.registerCodec(new CommandActorPushMessageCodec());
        return handler;
    }

    @Override
    public ActorServerContext check() {
        if (Objects.isNull(actorFactory)) {
            throw new MessagingException(" actor factory not found !");
        }
        if (Objects.isNull(actorMessageCodec)) {
            throw new MessagingException(" actor message codec not found !");
        }
        return this;
    }

    /**
     * 全局广播
     *
     * @param message
     */
    public void notifyActorMessage(Object message) {
        eventLoop().notifyWriteMessage(ActorChannel.type(), message);
        NodeActorNotifyMessage notifyMessage = new NodeActorNotifyMessage();
        notifyMessage.setSource(localDestination());
        notifyMessage.setContent(actorMessageCodec().encode(message));
        eventLoop().notifyWriteMessage(NodeChannel.type(), notifyMessage);
    }

    /**
     * 发送给指定的actor消息
     *
     * @param destination
     * @param message
     */
    public void pushActorMessage(ActorDestination destination, Object message) {
        ActorChannelEventLoop eventLoop = ((ActorChannelEventLoop) eventLoop());
        Actor actor = eventLoop.actorGroup().getActor(destination);
        if (null != actor) {
            actor.writeMessage(message);
        } else {
            NodeActorPushMessage pushMessage = new NodeActorPushMessage();
            pushMessage.setContent(eventLoop.actorMessageCodec().encode(message));
            pushMessage.setDestination(destination);
            pushMessage.setSource(localNode().destination());
            ChannelEvent event = new ChannelEvent(NodeChannel.type(), ChannelEventType.CHANNEL_WRITE, destination.getDestination(), pushMessage);
            eventLoop.channelEvent(event);
        }

    }

}
