package com.lockdown.messaging.actor.framework;

import java.io.Serializable;

public interface ActorMessageCodec<T extends Serializable> {


    public byte[] encode(T message);


    public T decode(byte[] content);

}
