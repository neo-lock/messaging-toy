package com.lockdown.messaging.cluster.node;

public interface RemoteMonitoringNodeBeanFactory extends RemoteNodeBeanFactory {


    void registerNodeMonitorSlot(NodeMonitorSlot monitorSlot);


}
