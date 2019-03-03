package com.lockdown.messaging.actor.framework;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class JsonMessageCodec implements ActorMessageCodec {


    @Override
    public byte[] encode(Serializable message) {
        return JSON.toJSONBytes(message);
    }

    @Override
    public Serializable decode(byte[] content) {
        return null;
    }
}
