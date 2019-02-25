package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.command.CommandType;

public interface NodeCommandInvoker<T> extends NodeCommandExecutor<T> {

    CommandType supportType();

}
