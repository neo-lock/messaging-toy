package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.node.RemoteNodeBeanFactory;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

public class RemoteNodeCommandHandler extends AbstractNodeHandler {


    public RemoteNodeCommandHandler(RemoteNodeBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        ServerDestination destination = new ServerDestination(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
        serverNode = beanFactory.getNodeInstance(ctx.newSucceededFuture(), destination);
    }


}
