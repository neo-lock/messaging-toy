package com.lockdown.messaging.cluster.framwork;


public interface ChannelMonitoringBeanFactory<T extends ChannelSlot, D extends Destination,M extends MessageForwardSlot> extends ChannelBeanFactory<T, D> {


    void setMonitorSlot(M forwardSlot);

}
