package com.lockdown.messaging.example.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMessageEncoder extends MessageToByteEncoder<JsonMessage> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void encode(ChannelHandlerContext ctx, JsonMessage jsonMessage, ByteBuf byteBuf) throws Exception {
        int length = jsonMessage.getContent().length;
        byteBuf.writeInt(length+4);
        byteBuf.writeBytes(jsonMessage.getContent());
    }
}
