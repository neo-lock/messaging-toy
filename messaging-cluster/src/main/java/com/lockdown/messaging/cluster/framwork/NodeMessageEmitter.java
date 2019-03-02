package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;

public interface NodeMessageEmitter extends MessageEmitter<ServerDestination,SourceNodeCommand> {
}
