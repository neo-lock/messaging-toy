package com.lockdown.messaging.cluster.channel.support;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.channel.LocalChannelContext;
import com.lockdown.messaging.cluster.channel.LocalChannelHandler;
import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeRegisterHandler implements LocalChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(LocalChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        if(event.getParam() instanceof NodeRegister){
            NodeRegister command = (NodeRegister) event.getParam();
            LocalNode localNode = ctx.pipeline().channel().localNode();
            logger.info("开始监控{}",command.getSource());
            localNode.monitor(command.getSource());
            localNode.printNodes();
            writeMonitor(ctx,command);
            logger.info("Notify All Channel");
            notifyChannel(ctx,command);
        }else{
            ctx.fireChannelReceived(message);
        }
    }
    private void writeMonitor(LocalChannelContext ctx,NodeRegister register){
        LocalNode localNode = ctx.pipeline().channel().localNode();
        NodeMonitored monitored = new NodeMonitored(localNode.destination());
        ChannelEvent monitorEvent = new ChannelEvent(ChannelEventType.CHANNEL_WRITE,register.getSource(),monitored);
        ctx.eventLoop().channelEvent(monitorEvent);
    }

    private void notifyChannel(LocalChannelContext ctx,NodeRegister register){
        LocalNode localNode = ctx.pipeline().channel().localNode();
        NodeRegisterForward registerForward = new NodeRegisterForward(localNode.destination(), register.getSource());
        ctx.eventLoop().nodeChannelGroup().nodeChannels().forEach(nodeChannel -> {
            if(nodeChannel.destination().equals(register.getSource())){
                return;
            }
            if(NodeChannel.class.isAssignableFrom(nodeChannel.getClass())){
                NodeChannel channel = (NodeChannel) nodeChannel;
                channel.writeAndFlush(registerForward);
            }
        });
    }


}
