package com.lockdown.messaging.cluster.sockethandler;
import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.node.RemoteServerNode;
import com.lockdown.messaging.cluster.node.ServerNodeEventListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeCommandHandler extends ChannelInboundHandlerAdapter {


    private RemoteServerNode serverNode;
    private final ServerNodeEventListener eventListener;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public LocalNodeCommandHandler(ServerNodeEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        serverNode = ServerNodeFactory.getRemoteNodeInstance(ctx.channel());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info(" 收到消息 : {} {}",msg.getClass(),JSON.toJSONString(msg));
        if(!(msg instanceof NodeCommand)){
            throw new UnsupportedOperationException(" unsupported message "+msg.getClass());
        }
        if(msg instanceof NodeRegister){
            NodeRegister registerCommand = (NodeRegister) msg;
            serverNode.applyDestination(registerCommand.getSource());
            eventListener.nodeRegistered(serverNode);
        }else{
            eventListener.commandEvent(serverNode, (NodeCommand) msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        eventListener.inactive(serverNode.destination());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(" channel exception {}",cause.getMessage());
        ctx.close();
    }
}
