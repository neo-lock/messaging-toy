package com.lockdown.messaging.cluster.command;

public class SyncNodeRegister implements SyncCommand {


    private String commandId;
    private NodeRegister registerCommand;

    public SyncNodeRegister() {
    }

    public SyncNodeRegister(String commandId, NodeRegister registerCommand) {
        this.commandId = commandId;
        this.registerCommand = registerCommand;
    }

    @Override
    public String getCommandId() {
        return commandId;
    }

    @Override
    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    @Override
    public SourceNodeCommand getOriginCommand() {
        return registerCommand;
    }

    @Override
    public void setOriginCommand(SourceNodeCommand command) {
        if (!(command instanceof NodeRegister)) {
            throw new IllegalArgumentException(" accept " + NodeRegister.class + " param !");
        }
        this.registerCommand = (NodeRegister) command;
    }

    public NodeRegister getRegisterCommand() {
        return registerCommand;
    }

    public void setRegisterCommand(NodeRegister registerCommand) {
        this.registerCommand = registerCommand;
    }

    @Override
    public CommandType type() {
        return CommandType.SYNC_REGISTER_ASK;
    }

    @Override
    public String toString() {
        return "SyncNodeRegister{" +
                "commandId='" + commandId + '\'' +
                ", registerCommand=" + registerCommand +
                '}';
    }
}
