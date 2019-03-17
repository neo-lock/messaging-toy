package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.Destination;

public interface EventSource {


    public Enum<?> channelType();

    public Destination destination();
}
