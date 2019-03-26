package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.exception.MessagingException;

import java.util.HashMap;
import java.util.Map;

public class NodeCommandCodecHandler implements CommandCodecHandler {


    private Map<Short, CommandCodec> codecMap = new HashMap<>();

    @Override
    public NodeCommand decode(short messageType, byte[] content) {
        checkType(messageType);
        return codecMap.get(messageType).bytesToCommand(content);
    }

    @Override
    public byte[] encode(NodeCommand command) {
        checkType(command.type().getType());
        return codecMap.get(command.type().getType()).commandToBytes(command);
    }

    protected void checkType(short type) {
        if (!codecMap.containsKey(type)) {
            throw new UnsupportedOperationException("unsupported command type number [" + type + "] !");
        }
    }

    @Override
    public void registerCodec(CommandCodec commandCodec) {
        if (null != codecMap.putIfAbsent(commandCodec.supportedType().getType(), commandCodec)) {
            throw new MessagingException(" codec type " + commandCodec.supportedType() + " exits !");
        }
    }
}
