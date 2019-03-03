package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.node.invoker.LocalServerNodeCommandExecutor;

public interface LocalNodeCommandExecutorFactory {

    LocalServerNodeCommandExecutor getInstance();

}
