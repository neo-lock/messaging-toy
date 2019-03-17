package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.DefaultChannelEventLoopInitializer;

public class ActorChannelEventLoopInitializer extends DefaultChannelEventLoopInitializer {


    @Override
    public void initialize(ChannelEventLoop eventLoop) {
        super.initialize(eventLoop);
        eventLoop.eventInvokerContext().registerEventInvoker(new ActorChannelEventInvoker(((ActorChannelEventLoop) eventLoop).actorChannelGroup()));
    }
}
