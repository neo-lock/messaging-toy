package com.lockdown.messaging.cluster.command;

public interface CommandCodec {


    NodeCommand bytesToCommand(byte[] content);


    byte[] commandToBytes(NodeCommand command);


    CommandType supportedType();

}
