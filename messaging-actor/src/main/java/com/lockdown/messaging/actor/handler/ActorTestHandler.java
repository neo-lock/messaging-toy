package com.lockdown.messaging.actor.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ActorTestHandler extends ByteToMessageCodec<String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
        logger.info(" 发送消息 {}",s);
        byteBuf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        String s = byteBuf.toString(StandardCharsets.UTF_8);
        logger.info(" 接收消息 {}",s);
        list.add(s);
    }
}
