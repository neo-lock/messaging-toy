package com.lockdown.messaging.cluster.command;

import com.lockdown.messaging.cluster.ServerDestination;

public interface NodeCommand {

    int BASE_LENGTH = 6;

    CommandType type();

    ServerDestination getSource();

}
