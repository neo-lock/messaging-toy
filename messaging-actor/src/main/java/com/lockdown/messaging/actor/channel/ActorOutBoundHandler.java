package com.lockdown.messaging.actor.channel;

public interface ActorOutBoundHandler {

    void channelWrite(Object message);

}
