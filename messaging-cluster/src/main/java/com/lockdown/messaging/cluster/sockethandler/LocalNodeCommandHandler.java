package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import io.netty.channel.ChannelHandlerContext;

public class LocalNodeCommandHandler extends AbstractNodeHandler {


    public LocalNodeCommandHandler(ServerContext serverContext) {
        super(serverContext);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (isLocalPort(ctx)) {
            serverNode = serverContext.nodeMonitor().getInstance(ctx.newSucceededFuture(), null);
        } else {
            ctx.fireChannelActive();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (isLocalPort(ctx)) {
            //serverContext.nodeMonitor().inactive(serverNode);
            serverNode.inactiveEvent();
        } else {
            ctx.fireChannelInactive();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (isLocalPort(ctx)) {
            //serverContext.nodeMonitor().exceptionCaught(serverNode,cause);
            serverNode.exceptionCaughtEvent(cause);
        } else {
            ctx.fireExceptionCaught(cause);
        }

    }


}
