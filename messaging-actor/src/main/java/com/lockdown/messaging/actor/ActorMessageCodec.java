package com.lockdown.messaging.actor;

public interface ActorMessageCodec {


    public Object decode(byte[] content);


    public byte[] encode(Object message);

}
