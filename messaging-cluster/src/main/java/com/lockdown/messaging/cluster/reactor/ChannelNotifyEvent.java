package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.command.NodeCommand;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChannelNotifyEvent {

    private Set<Destination> ignore = new HashSet<>();

    private final Object command;



    public ChannelNotifyEvent(Object command) {
        this.command = command;
    }

    public void addIgnore(Destination destination){
        this.ignore.add(destination);
    }

    public Object getCommand() {
        return command;
    }

    public Set<Destination> getIgnore() {
        return ignore;
    }
}
