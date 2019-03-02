package com.lockdown.messaging.cluster.framwork;


public interface SlotMonitoringBeanFactory<T extends ChannelSlot, D extends Destination,M extends MessageForwardSlot> extends SlotBeanFactory<T, D> {


    void setMonitorSlot(M forwardSlot);

}
