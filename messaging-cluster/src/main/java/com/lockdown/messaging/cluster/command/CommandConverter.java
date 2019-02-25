package com.lockdown.messaging.cluster.command;

public interface CommandConverter {


    NodeCommand bytesToCommand(byte[] content);


    byte[] CommandToBytes(NodeCommand command);

}
