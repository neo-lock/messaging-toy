package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.RegisterNature;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import io.netty.channel.ChannelHandlerContext;

public class LocalNodeCommandHandler extends AbstractNodeHandler {


    private ServerDestination destination;

    public LocalNodeCommandHandler(ServerContext serverContext) {
        super(serverContext);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (RegisterNature.class.isAssignableFrom(msg.getClass()) && null == destination) {
            SourceNodeCommand command = (SourceNodeCommand) msg;
            destination = command.getSource();
            serverContext.channelEventLoop().registerNodeChannel(ctx.newSucceededFuture(), destination);
        }
        super.channelRead(ctx, msg);
    }


    @Override
    protected ServerDestination getChannelDestination() {
        return destination;
    }

}
