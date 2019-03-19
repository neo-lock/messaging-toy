package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.CommandCodecHandler;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.regex.Pattern;

public class NodeCommandDecoder extends ByteToMessageDecoder {
    private Pattern nodeWhiteList;
    private CommandCodecHandler commandCodecHandler;

    public NodeCommandDecoder(ServerContext serverContext) {
        super();
        this.nodeWhiteList = serverContext.nodeWhiteList();
        this.commandCodecHandler = serverContext.codecHandler();
    }
    private Logger logger = LoggerFactory.getLogger(getClass());


    private boolean isLocalPort(ChannelHandlerContext ctx) {
        return IPUtils.isLocalPort(ctx,nodeWhiteList);
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (isLocalPort(ctx)) {
            SocketAddress address = ctx.channel().remoteAddress();
            logger.info("收到 {} 的消息 {}",address,byteBuf.readableBytes());
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
                    command = commandCodecHandler.decode(messageType,content);;
                } else {
                    command = commandCodecHandler.decode(messageType,null);
                }
                list.add(command);
            } catch (Exception ex) {
                ex.printStackTrace();
                ctx.fireExceptionCaught(ex);
            }
        } else {
            ReferenceCountUtil.retain(byteBuf);
            ctx.fireChannelRead(byteBuf);
        }
    }
}
