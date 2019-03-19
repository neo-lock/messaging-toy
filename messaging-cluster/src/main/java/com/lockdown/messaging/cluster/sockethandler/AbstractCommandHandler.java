package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class AbstractCommandHandler extends ChannelInboundHandlerAdapter {


    protected final ServerContext serverContext;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Pattern nodeWhiteList;


    public AbstractCommandHandler(ServerContext serverContext) {
        this.serverContext = serverContext;
        this.nodeWhiteList = serverContext.nodeWhiteList();
    }

    protected boolean isLocalPort(ChannelHandlerContext ctx) {
        return IPUtils.isLocalPort(ctx,nodeWhiteList);
    }


}
