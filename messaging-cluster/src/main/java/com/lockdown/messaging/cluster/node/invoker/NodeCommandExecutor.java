package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.RemoteNode;

public interface NodeCommandExecutor<T> {

    void executeCommand(T invoke, RemoteNode remote, NodeCommand command);

}
