package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.channel.support.ActorChannelEventLoopInitializer;
import com.lockdown.messaging.actor.channel.support.ActorDisruptorChannelEventLoop;
import com.lockdown.messaging.actor.channel.support.ActorRemoteNodeChannelInitializer;
import com.lockdown.messaging.actor.command.CommandActorMessageCodec;
import com.lockdown.messaging.cluster.AbstractServerContext;
import com.lockdown.messaging.cluster.command.CommandCodecHandler;
import com.lockdown.messaging.cluster.command.NodeCommandCodecHandler;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.NodeChannelInitializer;

import java.util.Objects;

public class ActorServerContext extends AbstractServerContext<ActorProperties> {

    private ActorMessageCodec actorMessageCodec;
    private Class<?> actorClass;
    private ActorFactory actorFactory;

    public ActorServerContext(ActorProperties properties) {
        super(properties);
    }

    @Override
    protected ChannelEventLoop initEventLoop() {
        return new ActorDisruptorChannelEventLoop(this, new ActorChannelEventLoopInitializer());
    }

    public ActorFactory actorFactory(){
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
        if(Objects.isNull(properties.getActorFactoryClassName())){
            throw new MessagingException("required ActorFactoryClassName");
        }
        Class<?> clazz = Class.forName(properties.getActorFactoryClassName());
        this.actorFactory = (ActorFactory) clazz.newInstance();
    }

    private void initActorMessageCodec() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (Objects.isNull(getProperties().getActorCodecClassName())) {
            throw new MessagingException("required ActorCodecClassName !");
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
        return handler;
    }
}
