package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;


public interface MessagingChannel {

    Destination destination();

    void handleEvent(ChannelEvent channelEvent);

    ChannelEventLoop eventLoop();


}
