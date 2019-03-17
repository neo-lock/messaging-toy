package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelGroup;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.support.ChannelTypeEventInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorChannelEventInvoker implements ChannelTypeEventInvoker {

    private final ActorChannelGroup actorChannelGroup;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ActorChannelEventInvoker(ActorChannelGroup actorChannelGroup) {
        this.actorChannelGroup = actorChannelGroup;
    }

    @Override
    public Enum<?> supported() {
        return ActorChannel.type();
    }

    /**
     * 这里需要处理找不到actor的情况!
     *
     * @param event
     */
    @Override
    public void handleEvent(ChannelEvent event) {
        Channel channel = actorChannelGroup.getChannel(event.getDestination());
        //这里应该抛出 找不到channel的 exception event，让对应的actor去进行处理,如果是消息的话，可以返回给发送方!
        if (null == channel) {
            logger.warn("{} 找不到channel!", event.getDestination());
            return;
        }
        actorChannelGroup.getChannel(event.getDestination()).handleEvent(event);
    }
}
