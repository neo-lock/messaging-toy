package com.lockdown.messaging.cluster.command;

public interface SyncCommand extends NodeCommand {


    public void setCommandId(String id);

    public String getCommandId();

    public SourceNodeCommand getOriginCommand();

    public void setOriginCommand(SourceNodeCommand command);

}
