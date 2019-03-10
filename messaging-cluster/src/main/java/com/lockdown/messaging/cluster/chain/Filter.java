package com.lockdown.messaging.cluster.chain;

import com.lockdown.messaging.cluster.framwork.ChannelSlot;

public interface Filter {

    void inactive(Chain chain, ChannelSlot target);

    void receivedMessage(Chain chain,ChannelSlot target,Object message);

    void exceptionCaught(Chain chain,ChannelSlot target,Throwable throwable);

    void close(Chain chain,ChannelSlot target,Reason reason);

}
