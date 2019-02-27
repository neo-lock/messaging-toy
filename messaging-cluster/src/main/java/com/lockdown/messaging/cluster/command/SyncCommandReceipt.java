package com.lockdown.messaging.cluster.command;

public class SyncCommandReceipt implements NodeCommand {

    private String commandId;

    public SyncCommandReceipt() {
    }

    public SyncCommandReceipt(String commandId) {
        this.commandId = commandId;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    @Override
    public CommandType type() {
        return CommandType.SYNC_RECEIPT;
    }
}
