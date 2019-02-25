package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NodeCommandDecoder extends ByteToMessageDecoder {


    private final int nodePort;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public NodeCommandDecoder(int nodePort) {
        super();
        this.nodePort = nodePort;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        logger.info(" received message with port {}", nodePort);
        try {
            int readable = byteBuf.readableBytes();
            if (readable < NodeCommand.BASE_LENGTH) {
                return;
            }

            byteBuf.markReaderIndex();
            int messageLength = byteBuf.readInt();
            if (readable < messageLength) {
                byteBuf.resetReaderIndex();
                return;
            }
            short messageType = byteBuf.readShort();

            int contentLength = messageLength - NodeCommand.BASE_LENGTH;

            NodeCommand command;

            if (contentLength > 0) {
                byte[] content = new byte[contentLength];
                byteBuf.readBytes(content);
                command = CommandType.typeOf(messageType).bytesToCommand(content);
            } else {
                command = CommandType.typeOf(messageType).bytesToCommand(null);
            }
            list.add(command);
        } catch (Exception ex) {
            ex.printStackTrace();
            ctx.close();
        }
    }
}
