package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class AbstractCommandHandler extends ChannelInboundHandlerAdapter {


    protected final ServerContext serverContext;
    private Pattern nodeWhiteList;

    protected Logger logger = LoggerFactory.getLogger(getClass());


    AbstractCommandHandler(ServerContext serverContext) {
        this.serverContext = serverContext;
        this.nodeWhiteList = serverContext.nodeWhiteList();
    }

    boolean isLocalPort(ChannelHandlerContext ctx) {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return nodeWhiteList.matcher(String.valueOf(localAddress.getPort())).matches() ||
                nodeWhiteList.matcher(String.valueOf(remoteAddress.getPort())).matches();
    }



}
