package com.lockdown.messaging.actor.framework;

import com.lockdown.messaging.cluster.framwork.ChannelSlot;
import com.lockdown.messaging.cluster.framwork.MessageAcceptor;

public interface ActorMessageAcceptor extends MessageAcceptor<ChannelSlot, Object> {
}
