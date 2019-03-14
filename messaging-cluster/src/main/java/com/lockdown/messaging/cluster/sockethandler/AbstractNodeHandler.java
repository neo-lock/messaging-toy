package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractNodeHandler extends AbstractCommandHandler {


    AbstractNodeHandler(ServerContext serverContext) {
        super(serverContext);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isLocalPort(ctx)) {
            ChannelEvent event = new ChannelEvent(ChannelEventType.CHANNEL_READ, getChannelDestination(), msg);
            serverContext.channelEventLoop().channelEvent(event);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (isLocalPort(ctx)) {
            if (null != getChannelDestination()) {
                ChannelEvent event = new ChannelEvent(ChannelEventType.CHANNEL_CLOSE, getChannelDestination(), new NodeClosed(getChannelDestination()));
                serverContext.channelEventLoop().channelEvent(event);
            }
        } else {
            ctx.fireChannelInactive();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (isLocalPort(ctx)) {
            cause.printStackTrace();
            ctx.close();
        } else {
            ctx.fireExceptionCaught(cause);
        }

    }

    protected abstract ServerDestination getChannelDestination();

}
