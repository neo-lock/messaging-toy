package com.lockdown.messaging.cluster.reactor;

import com.lockdown.messaging.cluster.channel.MessagingChannel;

public interface Dispatcher {

    public boolean registerChannel(MessagingChannel channel);

}
