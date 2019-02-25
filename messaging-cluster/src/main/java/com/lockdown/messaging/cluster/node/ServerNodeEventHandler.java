package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;

import java.util.Objects;

public class ServerNodeEventHandler implements ServerNodeEventListener {


    private ServerNodeEventListener eventListener;


    public void setEventListener(ServerNodeEventListener eventListener) {
        if(Objects.isNull(eventListener)){
            throw new IllegalStateException(" event listener already set !");
        }
        this.eventListener = eventListener;
    }

    @Override
    public void nodeRegistered(RemoteServerNode remoteServerNode) {
        if(Objects.nonNull(eventListener)){
            eventListener.nodeRegistered(remoteServerNode);
        }

    }

    @Override
    public void inactive(ServerDestination destination) {
        if(Objects.nonNull(eventListener)){
            eventListener.inactive(destination);
        }
    }


    @Override
    public void commandEvent(RemoteServerNode serverNode, NodeCommand command)  {
        if(Objects.nonNull(eventListener)){
            eventListener.commandEvent(serverNode,command);
        }
    }
}
