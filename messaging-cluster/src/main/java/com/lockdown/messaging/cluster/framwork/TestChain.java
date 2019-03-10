package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.node.ClusterRemoteNode;

public class TestChain {
    public static void main(String[] args){

        ActorChannelHandler actorChannelHandler = new ActorChannelHandler();
        actorChannelHandler.addFilter(new ActorNodeMonitor());
        actorChannelHandler.addFilter(new ChannelSlotHandler());
        actorChannelHandler.fireReceivedMessage(new ClusterRemoteNode(null,null,null),"hello");
    }
}
