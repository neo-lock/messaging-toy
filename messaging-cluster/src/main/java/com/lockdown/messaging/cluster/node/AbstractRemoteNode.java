package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;

import java.util.Objects;

public abstract class AbstractRemoteNode implements RemoteNode {


    private final ChannelFuture channelFuture;
    private ServerDestination destination;


    AbstractRemoteNode(ChannelFuture channelFuture, ServerDestination destination) {
        this.channelFuture = channelFuture;
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
        if (Objects.isNull(destination)) {
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
    public ChannelId channelId() {
        return channelFuture.channel().id();
    }

}
