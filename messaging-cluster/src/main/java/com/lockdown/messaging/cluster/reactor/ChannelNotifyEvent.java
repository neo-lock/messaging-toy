package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;

import java.util.HashSet;
import java.util.Set;

public class ChannelNotifyEvent {

    private final Object command;
    private Set<Destination> ignore = new HashSet<>();


    public ChannelNotifyEvent(Object command) {
        this.command = command;
    }

    public void addIgnore(Destination destination) {
        this.ignore.add(destination);
    }

    public Object getCommand() {
        return command;
    }

    public Set<Destination> getIgnore() {
        return ignore;
    }
}
