package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.node.invoker.*;

public class ClusterNodeCommandExecutorFactory implements NodeCommandExecutorFactory<LocalNode> {


    @Override
    public NodeCommandExecutor<LocalNode> getInstance() {
        LocalServerNodeCommandExecutor commandExecutor = new LocalServerNodeCommandExecutor();
        commandExecutor.registerInvoker(new NodeRegisterInvoker());
        commandExecutor.registerInvoker(new NodeRegisterForwardInvoker());
        commandExecutor.registerInvoker(new NodeMonitoredInvoker());
        commandExecutor.registerInvoker(new NodeClosedInvoker());
        return commandExecutor;
    }

}
