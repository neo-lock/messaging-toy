package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorMessageCodec;
import java.nio.charset.StandardCharsets;

public class SimpleStringCodec implements ActorMessageCodec {


    @Override
    public byte[] encode(Object message) {
        return message.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Object decode(byte[] content) {
        return new String(content, StandardCharsets.UTF_8);
    }
}
