package com.lockdown.messaging.cluster.command;

public interface CommandConverter {


    NodeCommand bytesToCommand(byte[] content);


    byte[] commandToBytes(NodeCommand command);

}
