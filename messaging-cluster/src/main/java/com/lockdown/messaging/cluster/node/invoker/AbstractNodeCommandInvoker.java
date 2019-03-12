package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.channel.MessagingChannel;
import com.lockdown.messaging.cluster.node.LocalNode;

public abstract class AbstractNodeCommandInvoker implements NodeCommandInvoker<LocalNode> {


    private final MessagingChannel channel;

    AbstractNodeCommandInvoker(MessagingChannel channel) {
        this.channel = channel;
    }


    protected MessagingChannel getChannel() {
        return this.channel;
    }


}
