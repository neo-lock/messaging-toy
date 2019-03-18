package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.exception.MessagingSerializeException;
import com.lockdown.messaging.cluster.sockethandler.ProtostuffUtils;

public class CommandClosedCodec extends AbstractCommandCodec {


    @Override
    public CommandType supportedType() {
        return DefaultCommandType.CLOSED;
    }

    @Override
    public NodeCommand bytesToCommand(byte[] content) {
        try {
            checkContent(content);
            return ProtostuffUtils.bytesToMessage(content, NodeClosed.class);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new MessagingSerializeException(e);
        }
    }

    @Override
    public byte[] commandToBytes(NodeCommand command) {
        return ProtostuffUtils.messageToBytes(command);
    }
}
