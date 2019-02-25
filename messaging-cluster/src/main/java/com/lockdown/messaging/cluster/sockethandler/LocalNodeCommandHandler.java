package com.lockdown.messaging.cluster.sockethandler;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.RegisterNature;
import com.lockdown.messaging.cluster.node.RemoteServerNode;
import com.lockdown.messaging.cluster.node.ServerNodeEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeCommandHandler extends ChannelInboundHandlerAdapter {


    private final ServerNodeEventListener eventListener;
    private RemoteServerNode serverNode;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public LocalNodeCommandHandler(ServerNodeEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverNode = ServerNodeFactory.getRemoteNodeInstance(ctx.channel().newSucceededFuture());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(" 收到消息 : {} {}", msg.getClass(), JSON.toJSONString(msg));
        if (!(msg instanceof NodeCommand)) {
            throw new UnsupportedOperationException(" unsupported message " + msg.getClass());
        }

        NodeCommand command = (NodeCommand) msg;

        if (RegisterNature.class.isAssignableFrom(command.getClass())) {
            serverNode.applyDestination(command.getSource());
            eventListener.nodeRegistered(serverNode, command);
        }
        eventListener.commandEvent(serverNode, (NodeCommand) msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        eventListener.inactive(serverNode.destination());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("============================error channel exception {}", cause.getMessage());
        ctx.close();
    }
}
