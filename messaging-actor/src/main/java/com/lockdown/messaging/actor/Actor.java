package com.lockdown.messaging.actor;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.MessageEmitter;

public interface Actor extends ChannelSlot<ActorDestination, Object>, MessageEmitter<ActorDestination, Object> {

    void receivedMessage(ActorDestination sender, Object message);

    void receivedMessage(Object message);

}
