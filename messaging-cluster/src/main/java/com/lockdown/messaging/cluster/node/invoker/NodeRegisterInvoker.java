package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalServerNode;
import com.lockdown.messaging.cluster.node.RemoteServerNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterInvoker implements NodeCommandInvoker<LocalServerNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_ASK;
    }


    @Override
    public void executeCommand(LocalServerNode local, RemoteServerNode remote, NodeCommand command) {
        if (local.isMonitored()) {
            logger.info(" 当前节点已经存在监控，开始转发 ");
            local.notifyRemote(new NodeRegisterForward(remote.destination()), remote.destination());
        } else {
            logger.info(" 开始监控节点 {}", remote.destination());
            local.monitor(remote.destination());
            local.sendCommand(remote.destination(), new NodeMonitored(local.destination()));
        }
    }
}
