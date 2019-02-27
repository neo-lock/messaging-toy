package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public interface SourceNodeCommand extends NodeCommand {

    ServerDestination getSource();

}
