package com.lockdown.messaging.cluster.node;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandExecutor;

public interface NodeCommandExecutorFactory<T> {

    NodeCommandExecutor<T> getInstance();

}
