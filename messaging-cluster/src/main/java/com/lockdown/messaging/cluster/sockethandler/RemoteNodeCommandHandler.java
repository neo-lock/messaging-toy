package com.lockdown.messaging.cluster.sockethandler;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.RemoteNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class RemoteNodeCommandHandler extends AbstractNodeHandler {


    public RemoteNodeCommandHandler(MessagingNodeContext serverContext) {
        super(serverContext);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        ServerDestination destination = new ServerDestination(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
        serverNode = serverContext.getNodeMonitor().newRemoteNodeInstance(ctx.channel().newSucceededFuture(),destination);
    }



}
