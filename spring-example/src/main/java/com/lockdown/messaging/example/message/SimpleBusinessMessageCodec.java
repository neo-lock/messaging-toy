package com.lockdown.messaging.example.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lockdown.messaging.actor.ActorMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class SimpleBusinessMessageCodec implements ActorMessageCodec {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public byte[] encode(Object message) {
        logger.info(" encode message {}",message);
        return JSON.toJSONBytes(message);
    }

    @Override
    public Object decode(byte[] content) {
        JSONObject object = (JSONObject) JSON.parse(content);
        if (!object.containsKey("type")) {
            throw new IllegalStateException(" error message !");
        }
        Class<?> clazz = MessageType.stringValueOf(object.getString("type")).getClazz();
        return JSON.toJavaObject(object,clazz);
    }
}
