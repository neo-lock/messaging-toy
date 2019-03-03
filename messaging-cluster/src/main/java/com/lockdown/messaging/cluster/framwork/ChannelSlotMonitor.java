package com.lockdown.messaging.cluster.framwork;

import java.util.Collection;


public interface ChannelSlotMonitor<T extends ChannelSlot, D extends Destination> extends MonitorUnit<T> {

    T findByDestination(D destination);

    Collection<T> AllDestination();

    void printNodes();

    void shutdown();

}
