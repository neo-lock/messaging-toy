package com.lockdown.messaging.cluster.node.invoker;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeMonitored;
import com.lockdown.messaging.cluster.command.NodeRegisterForward;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRegisterInvoker implements NodeCommandInvoker<LocalNode> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandType supportType() {
        return CommandType.REGISTER_ASK;
    }


    @Override
    public void executeCommand(LocalNode local, RemoteNode remote, NodeCommand command) {
        local.forceMonitor(remote.destination());
        local.sendCommand(remote.destination(), new NodeMonitored(local.destination()));
        local.notifyRemote(new NodeRegisterForward(local.destination(),remote.destination()), remote.destination());

    }
}
