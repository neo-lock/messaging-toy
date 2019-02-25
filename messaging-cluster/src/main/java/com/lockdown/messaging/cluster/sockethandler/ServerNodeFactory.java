package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.node.DefaultRemoteServerNode;
import com.lockdown.messaging.cluster.node.RemoteServerNode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class ServerNodeFactory {


    public static RemoteServerNode getRemoteNodeInstance(Channel channel, ServerDestination destination){
        return new DefaultRemoteServerNode(channel,destination);
    }

    public static RemoteServerNode getRemoteNodeInstance(Channel channel){
        return new DefaultRemoteServerNode(channel);
    }

}
