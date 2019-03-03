package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class RemoteNodeCommandHandler extends AbstractNodeHandler {


    public RemoteNodeCommandHandler(ServerContext serverContext) {
        super(serverContext);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (isLocalPort(ctx)) {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            ServerDestination destination = new ServerDestination(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
            serverNode = serverContext.nodeMonitor().getInstance(ctx.newSucceededFuture(), destination);
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
