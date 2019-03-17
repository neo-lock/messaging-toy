package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelGroup;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.support.ChannelTypeEventInvoker;

public class ActorChannelEventInvoker implements ChannelTypeEventInvoker {


    private final ActorChannelGroup actorChannelGroup;

    public ActorChannelEventInvoker(ActorChannelGroup actorChannelGroup) {
        this.actorChannelGroup = actorChannelGroup;
    }

    @Override
    public Enum<?> supported() {
        return ActorChannel.type();
    }

    /**
     * 这里需要处理找不到actor的情况!
     * @param event
     */
    @Override
    public void handleEvent(ChannelEvent event) {
        actorChannelGroup.getChannel(event.getDestination()).handleEvent(event);
    }
}
