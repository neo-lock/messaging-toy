package com.lockdown.messaging.actor.channel;

import com.lockdown.messaging.actor.channel.support.ChannelEventUnpackHandler;
import com.lockdown.messaging.cluster.channel.ChannelInitializer;

public class ActorChannelInitializer implements ChannelInitializer<ActorChannel> {


    @Override
    public void initialize(ActorChannel channel) {
        channel.pipeline().addLast(new ChannelEventUnpackHandler());
    }
}
