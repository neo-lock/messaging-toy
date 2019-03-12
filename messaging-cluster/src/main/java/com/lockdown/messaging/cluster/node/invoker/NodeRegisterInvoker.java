package com.lockdown.messaging.cluster.node.invoker;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.channel.RemoteNodeChannel;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterInvoker extends AbstractNodeCommandInvoker {

    private Logger logger = LoggerFactory.getLogger(getClass());

    NodeRegisterInvoker(MessagingChannel channel) {
        super(channel);
    }


    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_ASK;
    }

    @Override
    public void executeCommand(LocalNode local, SourceNodeCommand command) {
        logger.debug("收到注册请求 {}", JSON.toJSONString(command));
        local.forceMonitor(command.getSource());
        ((RemoteNodeChannel)getChannel().eventLoop().nodeChannelGroup().getNodeChannel(command.getSource())).writeAndFlush(new NodeMonitored(local.destination()));
        logger.info("通知所有节点");
        NodeRegisterForward registerForward = new NodeRegisterForward(local.destination(), command.getSource());
        getChannel().eventLoop().nodeChannelGroup().nodeChannels().forEach(remoteNodeChannel -> {
            if (remoteNodeChannel.destination().equals(command.getSource())) {
                return;
            }
            ((RemoteNodeChannel)remoteNodeChannel).writeAndFlush(registerForward);
        });
    }

}
