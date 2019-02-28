package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.NodeRegister;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.command.SyncNodeRegister;
import com.lockdown.messaging.cluster.support.MessageSync;
import io.netty.channel.ChannelFuture;

public class MonitoringNode extends AbstractRemoteNode {

    private final NodeMonitorSlot monitorSlot;


    public MonitoringNode(NodeMonitorSlot monitorSlot, ChannelFuture channelFuture, ServerDestination destination) {
        super(channelFuture, destination);
        this.monitorSlot = monitorSlot;
    }

    @MessageSync(originParam = NodeRegister.class,convertTo = SyncNodeRegister.class)
    @Override
    public void sendCommand(NodeCommand command) {
        super.sendCommand(command);
    }

    @Override
    public void receivedCommandEvent(SourceNodeCommand command) {
        monitorSlot.receivedCommand(this, command);
    }

    @Override
    public void inactiveEvent() {
        monitorSlot.inactive(this);
    }

    @Override
    public void exceptionCaughtEvent(Throwable cause) {
        monitorSlot.exceptionCaught(this, cause);
    }


}
