package com.lockdown.messaging.example.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonMessageDecoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        try {
            int readable = byteBuf.readableBytes();
            if (readable < 4) {
                return;
            }
            byteBuf.markReaderIndex();
            int messageLength = byteBuf.readInt();
            if (readable < messageLength) {
                byteBuf.resetReaderIndex();
                return;
            }

            int contentLength = messageLength - 4;
            if (contentLength > 0) {
                byte[] content = new byte[contentLength];
                byteBuf.readBytes(content);
                JSONObject object = (JSONObject) JSON.parse(content);

                if (!object.containsKey("type")) {
                    throw new IllegalStateException(" error message !");
                }
                MessageType type = MessageType.stringValueOf(object.getString("type"));
                list.add(JSON.toJavaObject(object, type.getClazz()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.close();
        }
    }
}
