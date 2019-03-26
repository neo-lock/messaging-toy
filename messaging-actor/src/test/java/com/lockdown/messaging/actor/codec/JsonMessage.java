package com.lockdown.messaging.actor.codec;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;

public class JsonMessage {

    private int length;

    private byte[] content;


    private JsonMessage(BusinessMessage message) {
        this.content = JSON.toJSONBytes(message);
        this.length = this.content.length;
    }

    public static JsonMessage wrap(BusinessMessage message) {
        return new JsonMessage(message);
    }

    @Override
    public String toString() {
        return "JsonMessage{" +
                "length=" + length +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
