package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeCommandEncoder extends MessageToByteEncoder<NodeCommand> {


    private final int nodePort;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public NodeCommandEncoder(int nodePort) {
        super();
        this.nodePort = nodePort;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NodeCommand command, ByteBuf byteBuf) throws Exception {
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


    }
}
