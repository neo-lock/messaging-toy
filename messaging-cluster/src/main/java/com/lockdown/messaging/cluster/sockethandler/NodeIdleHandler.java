package com.lockdown.messaging.cluster.sockethandler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

public class NodeIdleHandler extends IdleStateHandler {

    public NodeIdleHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds) {
        super(readerIdleTimeSeconds, writerIdleTimeSeconds, 0);
    }


    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        IdleState state = evt.state();

    }
}
