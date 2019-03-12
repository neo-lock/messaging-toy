package com.lockdown.messaging.cluster.node.invoker;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.channel.support.AbstractMessagingChannel;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNodeMessagingChannel extends AbstractMessagingChannel {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final LocalNode localNode;
    private LocalServerNodeCommandExecutor commandExecutor;

    public LocalNodeMessagingChannel(ChannelEventLoop eventLoop, LocalNode localNode) {
        super(eventLoop);
        this.localNode = localNode;
        this.commandExecutor = new LocalServerNodeCommandExecutor();
        this.commandExecutor.registerInvoker(new NodeMonitoredInvoker(this));
        this.commandExecutor.registerInvoker(new NodeRegisterForwardInvoker(this));
        this.commandExecutor.registerInvoker(new NodeRegisterInvoker(this));
        this.commandExecutor.registerInvoker(new NodeClosedInvoker(this));
    }


    @Override
    protected void processEvent(ChannelEvent event) {
        commandExecutor.executeCommand(localNode, (SourceNodeCommand) event.getParam());
    }

    @Override
    public Destination destination() {
        return localNode.destination();
    }
}
