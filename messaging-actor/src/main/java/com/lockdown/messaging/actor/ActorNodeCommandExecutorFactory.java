package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.command.NodeActorCommandInvoker;
import com.lockdown.messaging.cluster.node.ClusterNodeCommandExecutorFactory;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.NodeCommandExecutorFactory;
import com.lockdown.messaging.cluster.node.invoker.LocalServerNodeCommandExecutor;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandExecutor;

public class ActorNodeCommandExecutorFactory extends ClusterNodeCommandExecutorFactory implements NodeCommandExecutorFactory<LocalNode> {

    private final ActorServerContext serverContext;

    public ActorNodeCommandExecutorFactory(ActorServerContext serverContext) {
        super();
        this.serverContext = serverContext;
    }

    @Override
    public NodeCommandExecutor<LocalNode> getInstance() {
        LocalServerNodeCommandExecutor commandExecutor = (LocalServerNodeCommandExecutor) super.getInstance();
        commandExecutor.registerInvoker(new NodeActorCommandInvoker(serverContext.actorMessageRouter()));
        return commandExecutor;
    }
}
