package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class NodeCommandEncoder extends MessageToByteEncoder<NodeCommand> {


    private Pattern nodeWhiteList;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public NodeCommandEncoder(ServerContext serverContext) {
        super();
        this.nodeWhiteList = serverContext.nodeWhiteList();
    }


    private boolean isLocalPort(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return nodeWhiteList.matcher(String.valueOf(localAddress.getPort())).matches() ||
                nodeWhiteList.matcher(String.valueOf(remoteAddress.getPort())).matches();
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, NodeCommand command, ByteBuf byteBuf) throws Exception {
        if (isLocalPort(ctx)) {
            try {
                byte[] content = command.type().CommandToBytes(command);
                byteBuf.writeInt(NodeCommand.BASE_LENGTH + content.length);
                byteBuf.writeShort(command.type().getType());
                if (content.length > 0) {
                    byteBuf.writeBytes(content);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        } else {
            logger.info("! 错误的消息处理=================");
        }

    }
}
