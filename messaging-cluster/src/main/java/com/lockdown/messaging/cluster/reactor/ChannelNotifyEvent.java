package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ChannelNotifyEvent {

    private final Object command;
    private boolean multiple;
    private Set<Destination> ignore = new HashSet<>();


    public ChannelNotifyEvent(Object command) {
        this.command = command;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public void addIgnore(Destination destination) {
        this.ignore.add(destination);
    }

    public void addIgnores(Destination... destinations) {
        ignore.addAll(Arrays.asList(destinations));
    }

    public Object getCommand() {
        return command;
    }

    public Set<Destination> getIgnore() {
        return ignore;
    }
}
