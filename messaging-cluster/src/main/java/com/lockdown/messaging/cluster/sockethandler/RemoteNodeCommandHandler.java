package com.lockdown.messaging.cluster.sockethandler;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.RemoteServerNode;
import com.lockdown.messaging.cluster.node.ServerNodeEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class RemoteNodeCommandHandler extends ChannelInboundHandlerAdapter {

    private final ServerNodeEventListener eventListener;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private RemoteServerNode serverNode;


    public RemoteNodeCommandHandler(ServerNodeEventListener eventListener) {
        this.eventListener = eventListener;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        ServerDestination destination = new ServerDestination(socketAddress.getAddress().getHostAddress(), socketAddress.getPort());
        serverNode = ServerNodeFactory.getRemoteNodeInstance(ctx.channel(), destination);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(" 收到消息 : {} {}", msg.getClass(), JSON.toJSONString(msg));
        eventListener.commandEvent(serverNode, (NodeCommand) msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        eventListener.inactive(serverNode.destination());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(" channel exception {}", cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
