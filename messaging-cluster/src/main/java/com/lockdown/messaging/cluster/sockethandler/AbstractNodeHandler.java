package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.RemoteNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNodeHandler extends ChannelInboundHandlerAdapter {


    protected final MessagingNodeContext serverContext;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected RemoteNode serverNode;


    public AbstractNodeHandler(MessagingNodeContext serverContext) {
        this.serverContext = serverContext;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof NodeCommand)) {
            throw new UnsupportedOperationException(" unsupported message " + msg.getClass());
        }
        serverNode.receivedCommandEvent((NodeCommand) msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        serverNode.inactiveEvent();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(" channel exception ==================  {}", cause.getMessage());
        serverNode.exceptionCaughtEvent(cause);
    }

}
