package com.lockdown.messaging.cluster.reactor;

import java.util.List;

public interface Selector {

    void addEvent(ChannelEvent event);

    List<ChannelEvent> select();
}
