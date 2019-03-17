package com.lockdown.messaging.actor;

public class TestActorCodec implements ActorMessageCodec {


    @Override
    public Object decode(byte[] content) {
        return null;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }
}
