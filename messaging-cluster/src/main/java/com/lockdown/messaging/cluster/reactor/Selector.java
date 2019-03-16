package com.lockdown.messaging.cluster.reactor;

import java.util.List;

@Deprecated
public interface Selector {

    void addEvent(ChannelEvent event);

    List<ChannelEvent> select();
}
