package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.exception.MessagingSerializeException;
import com.lockdown.messaging.cluster.sockethandler.ProtostuffUtils;

public class CommandMonitoredCodec extends AbstractCommandCodec {
    @Override
    public CommandType supportedType() {
        return DefaultCommandType.MONITORED;
    }

    @Override
    public NodeCommand bytesToCommand(byte[] content) {
        try {
            checkContent(content);
            return ProtostuffUtils.bytesToMessage(content, NodeMonitored.class);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new MessagingSerializeException(e);
        }
    }

    @Override
    public byte[] commandToBytes(NodeCommand command) {
        return ProtostuffUtils.messageToBytes(command);
    }
}
