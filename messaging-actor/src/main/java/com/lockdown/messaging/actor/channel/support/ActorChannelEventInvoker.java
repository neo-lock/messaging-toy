package com.lockdown.messaging.actor.channel.support;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.actor.channel.ActorChannelGroup;
import com.lockdown.messaging.actor.command.NodeActorNotifyMessage;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.reactor.ChannelEvent;
import com.lockdown.messaging.cluster.reactor.ChannelEventType;
import com.lockdown.messaging.cluster.reactor.ChannelNotifyEvent;
import com.lockdown.messaging.cluster.reactor.support.ChannelTypeEventInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

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
        logger.info("ActorChannelEvent handle event {}", JSON.toJSONString(event));
        switch (event.getEventType()){
            case CHANNEL_NOTIFY:{
                eventLoop.actorChannelGroup().printNodes();
                ChannelNotifyEvent notifyEvent = (ChannelNotifyEvent) event.getParam();
                eventLoop.actorChannelGroup().actorChannels().forEach(actorChannel -> {
                    actorChannel.writeAndFlush(notifyEvent.getCommand());
                });
                break;
            }
            case CHANNEL_CLOSE:{
                //不需要break,让default 触发pipe close event !!!
                eventLoop.actorChannelGroup().removeActorChannel(event.getDestination());
            }
            default:{
                Channel channel = eventLoop.actorChannelGroup().getChannel(event.getDestination());
                //这里应该抛出 找不到channel的 exception event，让对应的actor去进行处理,如果是消息的话，可以返回给发送方!
                if (null == channel) {
                    logger.warn("{} 找不到channel!", event.getDestination());
                    return;
                }
                channel.handleEvent(event);
            }
        }

    }
}
