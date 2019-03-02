package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.RemoteNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class AbstractNodeHandler extends ChannelInboundHandlerAdapter {


    protected final ServerContext serverContext;

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected RemoteNode serverNode;
    private Pattern nodeWhiteList;


    public AbstractNodeHandler(ServerContext serverContext) {
        this.serverContext = serverContext;
        this.nodeWhiteList = serverContext.nodeWhiteList();
    }

    protected boolean isLocalPort(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return nodeWhiteList.matcher(String.valueOf(localAddress.getPort())).matches() ||
                nodeWhiteList.matcher(String.valueOf(remoteAddress.getPort())).matches();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isLocalPort(ctx)) {
            if (SourceNodeCommand.class.isAssignableFrom(msg.getClass())) {
                serverNode.receivedMessageEvent((SourceNodeCommand) msg);
            } else {
                logger.info(" 不能处理的消息========> {}", msg.getClass());
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }



}
