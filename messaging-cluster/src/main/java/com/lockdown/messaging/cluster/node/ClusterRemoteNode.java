package com.lockdown.messaging.cluster.node;


import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.framwork.NodeMonitorUnit;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ClusterRemoteNode implements RemoteNode {

    private final NodeMonitorUnit monitorUnit;
    private final ChannelFuture channelFuture;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ServerDestination destination;

    public ClusterRemoteNode(ChannelFuture channelFuture, NodeMonitorUnit monitorUnit) {
        this(channelFuture, monitorUnit, null);
    }

    public ClusterRemoteNode(ChannelFuture channelFuture, NodeMonitorUnit monitorUnit, ServerDestination destination) {
        this.destination = destination;
        this.monitorUnit = monitorUnit;
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
        monitorUnit.acceptedMessage(this, message);
    }

    @Override
    public void inactiveEvent() {
        monitorUnit.inactive(this);
    }

    @Override
    public void exceptionCaughtEvent(Throwable cause) {
        monitorUnit.exceptionCaught(this, cause);
    }

    @Override
    public ChannelId channelId() {
        return channelFuture.channel().id();
    }

    @Override
    public Enum<?> slotType() {
        return RemoteNodeType.REMOTE_NODE;
    }

    @Override
    public ServerDestination destination() {
        return destination;
    }

    //@MessageSync(originParam = NodeRegister.class,convertTo = SyncNodeRegister.class)
    @Override
    public void writeMessage(SourceNodeCommand message) {
        channelFuture.channel().writeAndFlush(message);
    }

}
