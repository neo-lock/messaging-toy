package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.command.SyncCommandReceipt;
import io.netty.channel.ChannelHandlerContext;

public class SyncCommandHandler extends AbstractNodeHandler {


    public SyncCommandHandler(ServerContext serverContext) {
        super(serverContext);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isLocalPort(ctx)) {
            if (SyncCommandReceipt.class.isAssignableFrom(msg.getClass())) {
                SyncCommandReceipt receipt = (SyncCommandReceipt) msg;
                serverContext.runtimeEnvironment().syncCommandMonitor().releaseMonitor(receipt.getCommandId());
                return;
            }
            if (SyncCommand.class.isAssignableFrom(msg.getClass())) {
                SyncCommand command = (SyncCommand) msg;
                ctx.writeAndFlush(new SyncCommandReceipt(command.getCommandId()));
                ctx.fireChannelRead(command.getOriginCommand());
            } else {
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }

    }
}
