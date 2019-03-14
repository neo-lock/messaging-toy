package com.lockdown.messaging.cluster.channel.support;

import com.lockdown.messaging.cluster.channel.LocalChannelContext;
import com.lockdown.messaging.cluster.channel.LocalChannelHandler;
import com.lockdown.messaging.cluster.channel.NodeChannel;
import com.lockdown.messaging.cluster.command.NodeGreeting;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeRegisterForwardHandler implements LocalChannelHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelReceived(LocalChannelContext ctx, Object message) {
        ChannelEvent event = (ChannelEvent) message;
        if(event.getParam() instanceof NodeRegisterForward){
            logger.info(" 执行 register for ward!");
            LocalNode localNode = ctx.pipeline().channel().localNode();
            NodeRegisterForward registerForward = (NodeRegisterForward) event.getParam();
            if (localNode.isAttached() && !localNode.attachedCompareAndSet(registerForward.getSource(), registerForward.getTarget())) {
                logger.info("发送greeting");
                ((NodeChannel) ctx.eventLoop().nodeChannelGroup().getNodeChannel(registerForward.getTarget())).writeAndFlush(new NodeGreeting(localNode.destination()));
            } else {
                logger.debug("当前对象没有attached,开始向{}注册", registerForward.getTarget());
                localNode.registerToCluster(registerForward.getTarget());
            }
        }else{
            ctx.fireChannelReceived(message);
        }
    }




}
