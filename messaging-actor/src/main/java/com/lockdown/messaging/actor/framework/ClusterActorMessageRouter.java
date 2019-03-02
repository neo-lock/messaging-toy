package com.lockdown.messaging.actor.framework;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.cluster.ContextExecutor;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeActorCommand;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandInvoker;

import java.util.Objects;

public class ClusterActorMessageRouter implements ActorMessageRouter,NodeCommandInvoker<LocalNode> {


    private final ClusterActorMonitor actorMonitor;
    private final ContextExecutor executor;
    private final ActorMessageCodec<?> messageCodec;
    private ServerContext serverContext;

    public ClusterActorMessageRouter(ClusterActorMonitor actorMonitor, ContextExecutor executor, ActorMessageCodec<?> messageCodec) {
        this.actorMonitor = actorMonitor;
        this.executor = executor;
        this.messageCodec = messageCodec;
    }



    @Override
    public CommandType supportType() {
        return CommandType.ACTOR_MSG;
    }

    private void handleNodeCommand(NodeActorCommand actorCommand,RemoteNode node){
        ActorDestination receiver = new ActorDestination(serverContext.localDestination(),actorCommand.getReceiverChannel());
        ActorDestination sender = new ActorDestination(node.destination(),actorCommand.getSenderChannel());
        Object object = messageCodec.decode(actorCommand.getContent());

    }

    @Override
    public void executeCommand(LocalNode invoke, RemoteNode remote, NodeCommand command) {
        NodeActorCommand actorCommand = (NodeActorCommand) command;
        handleNodeCommand(actorCommand,remote);
    }

    @Override
    public void messageTriggered(Actor channelSlot, Object message) {
        
    }

    @Override
    public void registerAcceptor(ActorMessageAccepor acceptor) {

    }
}
