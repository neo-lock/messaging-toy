package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.node.LocalNode;

public abstract class AbstractNodeCommandInvoker implements NodeCommandExecutor<LocalNode> {


    private final Channel channel;

    AbstractNodeCommandInvoker(Channel channel) {
        this.channel = channel;
    }


    protected Channel getChannel() {
        return this.channel;
    }


}
