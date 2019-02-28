package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;

public interface CommandTrigger {


    void commandTriggered(RemoteNode node, SourceNodeCommand command);


    void registerAcceptor(CommandAcceptor acceptor);

}
