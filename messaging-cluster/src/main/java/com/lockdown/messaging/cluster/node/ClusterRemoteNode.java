package com.lockdown.messaging.cluster.node;


import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.NodeForwardSlot;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ClusterRemoteNode  implements RemoteNode {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ServerDestination destination;
    private final NodeForwardSlot forwardSlot;
    private final ChannelFuture channelFuture;

    public ClusterRemoteNode(ChannelFuture channelFuture,NodeForwardSlot forwardSlot) {
        this(channelFuture,forwardSlot,null);
    }

    public ClusterRemoteNode(ChannelFuture channelFuture, NodeForwardSlot forwardSlot, ServerDestination destination) {
        this.destination = destination;
        this.forwardSlot = forwardSlot;
        this.channelFuture = channelFuture;
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
    public void receivedMessageEvent(SourceNodeCommand message) {
        forwardSlot.receivedMessage(this,message);
    }

    @Override
    public void inactiveEvent() {
        forwardSlot.inactive(this);
    }

    @Override
    public void exceptionCaughtEvent(Throwable cause) {
        forwardSlot.exceptionCaught(this,cause);
    }

    @Override
    public ChannelId channelId() {
        return channelFuture.channel().id();
    }

    @Override
    public ServerDestination destination() {
        return destination;
    }

    @Override
    public void writeMessage(SourceNodeCommand message) {
        channelFuture.channel().writeAndFlush(message);
    }

}
