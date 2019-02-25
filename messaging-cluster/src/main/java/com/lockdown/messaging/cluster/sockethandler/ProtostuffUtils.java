package com.lockdown.messaging.cluster.sockethandler;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffUtils {

    public static <T> T bytesToMessage(byte[] bytes, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T message = clazz.newInstance();
        ProtostuffIOUtil.mergeFrom(bytes, message, schema);
        return message;
    }

    @SuppressWarnings("unchecked")
    public static <T> byte[] messageToBytes(T message) {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>) message.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] result;
        try {
            result = ProtostuffIOUtil.toByteArray(message, schema, buffer);
        } finally {
            buffer.clear();
        }
        return result;
    }


}
