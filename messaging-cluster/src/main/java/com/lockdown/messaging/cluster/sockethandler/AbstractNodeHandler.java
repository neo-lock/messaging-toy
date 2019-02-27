package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.command.SyncCommandReceipt;
import com.lockdown.messaging.cluster.node.RemoteNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

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
        if(SyncCommandReceipt.class.isAssignableFrom(msg.getClass())){
            SyncCommandReceipt receipt = (SyncCommandReceipt) msg;
            serverContext.releaseSyncMessage(receipt.getCommandId());
            return;
        }
        if(SyncCommand.class.isAssignableFrom(msg.getClass())){
            SyncCommand command = (SyncCommand) msg;
            ctx.writeAndFlush(new SyncCommandReceipt(command.getCommandId()));
            serverNode.receivedCommandEvent(command.getOriginCommand());
        }else if(SourceNodeCommand.class.isAssignableFrom(msg.getClass())){
            serverNode.receivedCommandEvent((SourceNodeCommand) msg);
        }else{
            throw new UnsupportedOperationException(" unsupported message " + msg.getClass());
        }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        serverNode.inactiveEvent();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("异常: {}", cause.getMessage());
        cause.printStackTrace();
        serverNode.exceptionCaughtEvent(cause);
    }

}
