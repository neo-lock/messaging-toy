package com.lockdown.messaging.actor.framework;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class JsonMessageCodec implements ActorMessageCodec {


    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }

    @Override
    public Object decode(byte[] content) {
        return null;
    }
}
