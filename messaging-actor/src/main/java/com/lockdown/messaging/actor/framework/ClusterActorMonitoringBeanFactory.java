package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.actor.ActorDestination;
import io.netty.channel.ChannelFuture;

public class ClusterActorMonitoringBeanFactory implements ActorMonitoringBeanFactory {




    @Override
    public void setMonitorSlot(ActorForwardSlot forwardSlot) {

    }

    @Override
    public Actor getInstance(ChannelFuture channelFuture, ActorDestination destination) {
        return null;
    }


}
