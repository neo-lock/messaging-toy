package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.actor.framework.ActorMessageAcceptor;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.MessageTrigger;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.node.RemoteNode;
import com.lockdown.messaging.cluster.node.invoker.NodeCommandInvoker;

import java.util.Objects;

public class NodeActorCommandInvoker implements NodeCommandInvoker<LocalNode>, MessageTrigger<ChannelSlot, Object, ActorMessageAcceptor> {


    private  ActorMessageAcceptor messageAcceptor;

    public NodeActorCommandInvoker(ActorMessageAcceptor messageAcceptor) {
        registerAcceptor(messageAcceptor);
    }


    @Override
    public CommandType supportType() {
        return CommandType.ACTOR_MSG;
    }

    @Override
    public void executeCommand(LocalNode invoke, RemoteNode remote, NodeCommand command) {
        messageTriggered(remote, command);
    }


    @Override
    public void messageTriggered(ChannelSlot channelSlot, Object message) {
        this.messageAcceptor.acceptedMessage(channelSlot, message);
    }


    @Override
    public void registerAcceptor(ActorMessageAcceptor acceptor) {
        if (Objects.isNull(acceptor)) {
            throw new IllegalArgumentException(" acceptor can't be null !");
        }
        if (Objects.nonNull(this.messageAcceptor)) {
            throw new IllegalStateException(" acceptor already set !");
        }
        this.messageAcceptor = acceptor;
    }


}
