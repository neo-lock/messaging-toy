package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;

public interface NodeCommandExecutor<T> {

    void executeCommand(T invoke, SourceNodeCommand command);

}
