package com.lockdown.messaging.cluster.reactor.support;

public interface ChannelTypeEventInvoker extends ChannelEventInvoker {

    Enum<?> supported();

}
