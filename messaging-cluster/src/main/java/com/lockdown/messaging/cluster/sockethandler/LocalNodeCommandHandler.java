package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.node.RemoteNodeBeanFactory;
import io.netty.channel.ChannelHandlerContext;

public class LocalNodeCommandHandler extends AbstractNodeHandler {


    public LocalNodeCommandHandler(RemoteNodeBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverNode = beanFactory.getNodeInstance(ctx.newSucceededFuture(), null);
    }

}
