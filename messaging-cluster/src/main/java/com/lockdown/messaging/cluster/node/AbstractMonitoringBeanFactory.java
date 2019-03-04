package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.Destination;
import com.lockdown.messaging.cluster.framwork.MonitorUnit;
import com.lockdown.messaging.cluster.framwork.SlotBeanFactory;

public abstract class AbstractMonitoringBeanFactory<T extends ChannelSlot, D extends Destination, M extends MonitorUnit> implements SlotBeanFactory<T, D> {
    abstract void setMonitorUnit(M monitorUnit);
}
