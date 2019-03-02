package com.lockdown.messaging.actor.framework;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.MessageEmitter;

public interface Actor extends ChannelSlot<ActorDestination,Object>, MessageEmitter<ActorDestination,Object> {



}
