package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;

public interface LocalNode extends ServerNode, CommandAcceptor {


    void registerToCluster(ServerDestination destination);

    boolean isMonitored();

    void notifyRemote(SourceNodeCommand command, ServerDestination... ignore);

    void sendCommand(ServerDestination target, SourceNodeCommand command);

    void monitor(ServerDestination destination);

    void attachTo(ServerDestination destination);

    boolean isAttached();

    boolean monitorCompareAndSet(ServerDestination old, ServerDestination update);

    boolean attachedCompareAndSet(ServerDestination old, ServerDestination update);

    void registerRandomNode();

    void printNodes();

    ServerDestination forceMonitor(ServerDestination destination);
}
