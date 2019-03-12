package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeClosed;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public abstract class AbstractNodeHandler extends AbstractCommandHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());
    AbstractNodeHandler(ServerContext serverContext) {
        super(serverContext);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (isLocalPort(ctx)) {
            logger.info(" 收到消息 {}",msg);
            if (SourceNodeCommand.class.isAssignableFrom(msg.getClass())) {
                serverContext.channelEventLoop().registerNodeChannel(ctx.newSucceededFuture(), getChannelDestination());
            }
            ChannelEvent event = new ChannelEvent(ChannelEventType.NODE,getChannelDestination(),msg);
            serverContext.channelEventLoop().channelEvent(event);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (isLocalPort(ctx)) {
            if (null != getChannelDestination()) {
                ChannelEvent event = new ChannelEvent(ChannelEventType.NODE, getChannelDestination(),new NodeClosed(getChannelDestination()));
                serverContext.channelEventLoop().channelEvent(event);
            }
        } else {
            ctx.fireChannelInactive();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(isLocalPort(ctx)){
            cause.printStackTrace();
            ctx.close();
        }else{
            ctx.fireExceptionCaught(cause);
        }

    }

    protected abstract ServerDestination getChannelDestination();

}
