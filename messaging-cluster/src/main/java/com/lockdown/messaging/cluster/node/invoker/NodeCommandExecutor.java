package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.RemoteServerNode;

public interface NodeCommandExecutor<T> {

    public void executeCommand(T invoke, RemoteServerNode remote, NodeCommand command);

}
