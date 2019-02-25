package com.lockdown.messaging.cluster.node;
import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;

public interface LocalServerNode extends ServerNode {


    void registerToCluster(ServerDestination destination);


    boolean isMonitored();

    void notifyRemote(NodeCommand command,ServerDestination...ignore);



    void sendCommand(ServerDestination target,NodeCommand command);

    void monitor(ServerDestination destination);

    void attachTo(ServerDestination destination);



    boolean isAttached();

    boolean matchClearMonitor(ServerDestination destination);

    boolean matchClearAttached(ServerDestination destination);
}
