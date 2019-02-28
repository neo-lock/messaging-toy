package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.command.SyncCommandReceipt;
import com.lockdown.messaging.cluster.node.RemoteNode;
import com.lockdown.messaging.cluster.node.RemoteNodeBeanFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNodeHandler extends ChannelInboundHandlerAdapter {


    protected final RemoteNodeBeanFactory beanFactory;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected RemoteNode serverNode;


    public AbstractNodeHandler(RemoteNodeBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (SourceNodeCommand.class.isAssignableFrom(msg.getClass())){
            serverNode.receivedCommandEvent((SourceNodeCommand) msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        serverNode.inactiveEvent();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("异常: {}", cause.getMessage());
        //cause.printStackTrace();
        serverNode.exceptionCaughtEvent(cause);
    }

}
