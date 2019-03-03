package com.lockdown.messaging.cluster.framwork;


public interface SlotMonitoringBeanFactory<T extends ChannelSlot, D extends Destination, M extends MonitorUnit> extends SlotBeanFactory<T, D> {


    void setMonitorUnit(M monitorUnit);

}
