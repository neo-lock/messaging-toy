package com.lockdown.messaging.cluster.command;

public interface SyncCommand extends NodeCommand {


    public String getCommandId();

    public void setCommandId(String id);

    public SourceNodeCommand getOriginCommand();

    public void setOriginCommand(SourceNodeCommand command);

}
