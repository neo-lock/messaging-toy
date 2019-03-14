package com.lockdown.messaging.cluster.channel;

import com.lockdown.messaging.cluster.node.LocalNode;

public interface LocalChannel extends Channel {

    LocalChannelPipeline pipeline();

    LocalNode localNode();

}
