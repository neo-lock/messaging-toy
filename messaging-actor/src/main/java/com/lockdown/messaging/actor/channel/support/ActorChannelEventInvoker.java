package com.lockdown.messaging.actor.channel.support;

import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelNotifyEvent;
import com.lockdown.messaging.cluster.reactor.support.ChannelTypeEventInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorChannelEventInvoker implements ChannelTypeEventInvoker {

    private final ActorChannelEventLoop eventLoop;
    private Logger logger = LoggerFactory.getLogger(getClass());

    ActorChannelEventInvoker(ActorChannelEventLoop eventLoop) {
        this.eventLoop = eventLoop;
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
        switch (event.getEventType()) {
            case CHANNEL_NOTIFY: {
                eventLoop.actorGroup().printNodes();
                ChannelNotifyEvent notifyEvent = (ChannelNotifyEvent) event.getParam();
                eventLoop.actorGroup().allActors().forEach(actor -> {
                    actor.writeMessage(notifyEvent.getCommand());
                });
                break;
            }
            case CHANNEL_CLOSE: {
                //不需要break,让default 触发pipe close event !!!
                eventLoop.actorGroup().removeActor(event.getDestination());
            }
            default: {
                Actor actor = eventLoop.actorGroup().getActor(event.getDestination());
                //这里应该抛出 找不到channel的 exception event，让对应的actor去进行处理,如果是消息的话，可以返回给发送方!
                if (null == actor) {
                    logger.warn("{} 找不到channel!", event.getDestination());
                    return;
                }
                actor.channel().handleEvent(event);
            }
        }

    }
}
