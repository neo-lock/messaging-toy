package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.CommandCodecHandler;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.regex.Pattern;

public class NodeCommandEncoder extends MessageToByteEncoder<NodeCommand> {


    private Pattern nodeWhiteList;
    private CommandCodecHandler commandCodecHandler;

    public NodeCommandEncoder(ServerContext serverContext) {
        super();
        this.nodeWhiteList = serverContext.nodeWhiteList();
        this.commandCodecHandler = serverContext.codecHandler();
    }


    private boolean isLocalPort(ChannelHandlerContext ctx) {
        return IPUtils.isLocalPort(ctx, nodeWhiteList);
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, NodeCommand command, ByteBuf byteBuf) throws Exception {
        if (isLocalPort(ctx)) {
            try {
                byte[] content = commandCodecHandler.encode(command);
                byteBuf.writeInt(NodeCommand.BASE_LENGTH + content.length);
                byteBuf.writeShort(command.type().getType());
                if (content.length > 0) {
                    byteBuf.writeBytes(content);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }
}
