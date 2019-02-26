package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.MessagingNodeContext;
import io.netty.channel.ChannelHandlerContext;

public class LocalNodeCommandHandler extends AbstractNodeHandler {


    public LocalNodeCommandHandler(MessagingNodeContext serverContext) {
        super(serverContext);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverNode = serverContext.getNodeMonitor().newRemoteNodeInstance(ctx.newSucceededFuture());
    }

}
