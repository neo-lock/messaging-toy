package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Pattern;

public class NodeCommandDecoder extends ByteToMessageDecoder {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private Pattern nodeWhiteList;

    public NodeCommandDecoder(ServerContext serverContext) {
        super();
        this.nodeWhiteList = serverContext.nodeWhiteList();
    }


    private boolean isLocalPort(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("LocalAddress{},RemoteAddress{}", localAddress, remoteAddress);
        return nodeWhiteList.matcher(String.valueOf(localAddress.getPort())).matches() ||
                nodeWhiteList.matcher(String.valueOf(remoteAddress.getPort())).matches();
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (isLocalPort(ctx)) {
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
        } else {
            ReferenceCountUtil.retain(byteBuf);
            ctx.fireChannelRead(byteBuf);
        }
    }
}
