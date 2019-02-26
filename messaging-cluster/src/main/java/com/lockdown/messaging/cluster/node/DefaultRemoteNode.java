package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;

import java.util.Objects;

public class DefaultRemoteNode implements RemoteNode {


    private final RemoteNodeSlot remoteNodeSlot;
    private final ChannelFuture channelFuture;
    private ServerDestination destination;



    public DefaultRemoteNode(RemoteNodeSlot remoteNodeSlot, ChannelFuture channelFuture) {
        this.remoteNodeSlot = remoteNodeSlot;
        this.channelFuture = channelFuture;
    }

    public DefaultRemoteNode(RemoteNodeSlot remoteNodeSlot, ChannelFuture channelFuture, ServerDestination destination) {
        this(remoteNodeSlot,channelFuture);
        this.destination = destination;
    }

    @Override
    public ServerDestination destination() {
        return destination;
    }

    @Override
    public void sendCommand(NodeCommand command) {
        this.channelFuture.channel().writeAndFlush(command);
    }

    @Override
    public void applyDestination(ServerDestination destination) {
        if(Objects.isNull(destination)){
            throw new IllegalStateException(" destination can't be null !");
        }
        if (Objects.nonNull(this.destination)) {
            return;
        }
        this.destination = destination;
    }


    @Override
    public void close() {
        channelFuture.channel().close();
    }

    @Override
    public void receivedCommandEvent(NodeCommand msg) {
        remoteNodeSlot.receivedCommand(this,msg);
    }

    @Override
    public void inactiveEvent() {
        remoteNodeSlot.inactive(this);
    }

    @Override
    public void exceptionCaughtEvent(Throwable cause) {
        remoteNodeSlot.exceptionCaught(this,cause);
    }

    @Override
    public ChannelId channelId() {
        return channelFuture.channel().id();
    }


}
