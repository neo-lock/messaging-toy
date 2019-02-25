package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;

import java.util.Objects;

public class ServerNodeEventHandler implements ServerNodeEventListener {


    private ServerNodeEventListener eventListener;


    public void setEventListener(ServerNodeEventListener eventListener) {
        if(Objects.isNull(eventListener)){
            throw new IllegalArgumentException(" event listener can't not be null!");
        }
        if (Objects.nonNull(this.eventListener)) {
            throw new IllegalStateException(" event listener already set !");
        }
        this.eventListener = eventListener;
    }

    @Override
    public void nodeRegistered(RemoteServerNode remoteServerNode, NodeCommand command) {
        if (Objects.nonNull(eventListener)) {
            eventListener.nodeRegistered(remoteServerNode, command);
        }

    }

    @Override
    public void inactive(ServerDestination destination) {
        if (Objects.nonNull(eventListener)) {
            eventListener.inactive(destination);
        }
    }


    @Override
    public void commandEvent(RemoteServerNode serverNode, NodeCommand command) {
        if (Objects.nonNull(eventListener)) {
            eventListener.commandEvent(serverNode, command);
        }
    }
}
