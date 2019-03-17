package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.channel.support.ActorChannelEventLoopInitializer;
import com.lockdown.messaging.actor.channel.support.ActorDisruptorChannelEventLoop;
import com.lockdown.messaging.actor.channel.support.ActorRemoteNodeChannelInitializer;
import com.lockdown.messaging.cluster.AbstractServerContext;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.NodeChannelInitializer;

import java.util.Objects;

public class ActorServerContext extends AbstractServerContext<ActorProperties> {

    private ActorMessageCodec actorMessageCodec;
    private Class<?> actorClass;

    public ActorServerContext(ActorProperties properties) {
        super(properties);
    }

    @Override
    protected ChannelEventLoop initEventLoop() {
        return new ActorDisruptorChannelEventLoop(this, new ActorChannelEventLoopInitializer());
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
        } catch (Throwable t) {
            throw new MessagingException(t.getMessage());
        }
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
}
