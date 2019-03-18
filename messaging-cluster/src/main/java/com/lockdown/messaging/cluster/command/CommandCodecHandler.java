package com.lockdown.messaging.cluster.command;

public interface CommandCodecHandler {

    NodeCommand decode(short messageType, byte[] content);

    byte[] encode(NodeCommand command);

    void registerCodec(CommandCodec commandCodec);
}
