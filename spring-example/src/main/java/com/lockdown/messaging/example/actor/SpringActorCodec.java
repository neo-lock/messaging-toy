package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.framework.ActorMessageCodec;

public class SpringActorCodec implements ActorMessageCodec {


    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }

    @Override
    public Object decode(byte[] content) {
        return null;
    }
}
