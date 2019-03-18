package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class RemoteNodeCommandHandler extends AbstractNodeHandler {

    private ServerDestination destination;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public RemoteNodeCommandHandler(ServerContext serverContext) {
        super(serverContext);
    }

    @Override
    protected ServerDestination getChannelDestination() {
        return destination;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (isLocalPort(ctx)) {
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            destination = new ServerDestination(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
            serverContext.eventLoop().registerNodeChannel(ctx.newSucceededFuture(), destination);
        } else {
            ctx.fireChannelActive();
        }
    }


}
