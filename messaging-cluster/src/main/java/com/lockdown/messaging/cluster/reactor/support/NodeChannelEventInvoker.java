package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.channel.support.NodeChannel;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelNotifyEvent;
import com.lockdown.messaging.cluster.reactor.NodeChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeChannelEventInvoker implements ChannelTypeEventInvoker {

    private final NodeChannelGroup nodeChannelGroup;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public NodeChannelEventInvoker(NodeChannelGroup nodeChannelGroup) {
        this.nodeChannelGroup = nodeChannelGroup;
    }

    @Override
    public void handleEvent(ChannelEvent event) {
        switch (event.getEventType()) {
            case CHANNEL_NOTIFY: {
                ChannelNotifyEvent notifyEvent = (ChannelNotifyEvent) event.getParam();
                nodeChannelGroup.nodeChannels().forEach(nodeChannel -> {
                    if (notifyEvent.getIgnore().contains(nodeChannel.destination())) {
                        return;
                    }
                    nodeChannel.pipeline().writeAndFlush(notifyEvent.getCommand());
                });
                break;
            }
            default: {
                logger.info("收到的Destination {}", event.getDestination());
                if(!nodeChannelGroup.containsNode(event.getDestination())){
                    throw new MessagingException(" destination not found!");
                }
                nodeChannelGroup.getNodeChannel(event.getDestination()).handleEvent(event);
            }
        }
    }

    @Override
    public Enum<?> supported() {
        return NodeChannel.type();
    }

}
