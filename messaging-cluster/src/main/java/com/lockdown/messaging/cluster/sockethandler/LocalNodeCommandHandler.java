package com.lockdown.messaging.cluster.sockethandler;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.command.AbstractNodeCommand;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.RegisterNature;
import com.lockdown.messaging.cluster.node.RemoteNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeCommandHandler extends AbstractNodeHandler {


    public LocalNodeCommandHandler(MessagingNodeContext serverContext) {
        super(serverContext);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverNode = serverContext.getNodeMonitor().newRemoteNodeInstance(ctx.newSucceededFuture());
    }

}
