package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.exception.MessagingSerializeException;
import com.lockdown.messaging.cluster.sockethandler.ProtostuffUtils;

public class CommandRegisterForwardCodec extends AbstractCommandCodec{


    @Override
    public CommandType supportedType() {
        return DefaultCommandType.REGISTER_FORWARD;
    }

    @Override
    public NodeCommand bytesToCommand(byte[] content) {
        try {
            checkContent(content);
            return ProtostuffUtils.bytesToMessage(content, NodeRegisterForward.class);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new MessagingSerializeException(e);
        }
    }

    @Override
    public byte[] commandToBytes(NodeCommand command) {
        return ProtostuffUtils.messageToBytes(command);
    }
}
