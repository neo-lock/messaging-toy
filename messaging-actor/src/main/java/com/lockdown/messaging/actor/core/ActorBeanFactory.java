package com.lockdown.messaging.actor.core;


import com.lockdown.messaging.actor.ActorSlot;
import io.netty.channel.ChannelHandlerContext;

public interface ActorBeanFactory {


    public ActorSlot newInstance(ChannelHandlerContext ctx);


}
