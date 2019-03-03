package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public class NodeActorCommand extends AbstractNodeCommand {


    private final String receiverChannel;

    private final String senderChannel;

    private final byte[] content;

    public NodeActorCommand(ServerDestination source, String receiverChannel, String senderChannel, byte[] content) {
        super(source);
        this.receiverChannel = receiverChannel;
        this.senderChannel = senderChannel;
        this.content = content;
    }

    public String getReceiverChannel() {
        return receiverChannel;
    }

    public String getSenderChannel() {
        return senderChannel;
    }

    public byte[] getContent() {
        return content;
    }


    @Override
    public CommandType type() {
        return CommandType.ACTOR_MSG;
    }
}
