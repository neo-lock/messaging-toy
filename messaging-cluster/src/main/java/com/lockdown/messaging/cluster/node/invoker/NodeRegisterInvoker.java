package com.lockdown.messaging.cluster.node.invoker;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalServerNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterInvoker implements NodeCommandInvoker<LocalServerNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_ASK;
    }


    @Override
    public void executeCommand(LocalServerNode local, RemoteNode remote, NodeCommand command) {
        local.forceMonitor(remote.destination());
        local.sendCommand(remote.destination(), new NodeMonitored(local.destination()));
        local.notifyRemote(new NodeRegisterForward(local.destination(),remote.destination()), remote.destination());

    }
}
