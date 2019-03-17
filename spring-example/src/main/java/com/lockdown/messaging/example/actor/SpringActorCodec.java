package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorMessageCodec;

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
