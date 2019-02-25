package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRemoteServerNode extends AbstractServerNode implements RemoteServerNode {

    private final ChannelFuture channel;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultRemoteServerNode(ChannelFuture channel) {
        this.channel = channel;
    }

    public DefaultRemoteServerNode(ChannelFuture channel, ServerDestination destination) {
        this(channel);
        this.destination = destination;
    }

    @Override
    public void sendCommand(NodeCommand command) {
        logger.info(" current remote  destination {}", this.destination);
        this.channel.channel().writeAndFlush(command);
    }


    public void applyDestination(ServerDestination destination) {
        if (null != this.destination) {
            return;
        }
        this.destination = destination;
    }

    @Override
    public void close() {
        this.channel.channel().close();
    }


}
