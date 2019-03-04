package com.lockdown.messaging.actor.framework;

import java.io.Serializable;

public interface ActorMessageCodec{


    public byte[] encode(Object message);


    public Object decode(byte[] content);

}
