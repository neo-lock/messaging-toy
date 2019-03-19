package com.lockdown.messaging.actor.command;

import com.lockdown.messaging.cluster.command.AbstractCommandCodec;
import com.lockdown.messaging.cluster.command.CommandType;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.exception.MessagingSerializeException;
import com.lockdown.messaging.cluster.sockethandler.ProtostuffUtils;

public class CommandActorNotifyMessageCodec extends AbstractCommandCodec {
    @Override
    public NodeCommand bytesToCommand(byte[] content) {
        try {
            checkContent(content);
            return ProtostuffUtils.bytesToMessage(content, NodeActorNotifyMessage.class);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new MessagingSerializeException(e);
        }
    }

    @Override
    public byte[] commandToBytes(NodeCommand command) {
        return ProtostuffUtils.messageToBytes(command);
    }

    @Override
    public CommandType supportedType() {
        return ActorCommandType.ACTOR_NOTIFY;
    }
}
