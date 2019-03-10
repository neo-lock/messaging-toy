package com.lockdown.messaging.cluster.chain;

import com.lockdown.messaging.cluster.framwork.ChannelSlot;

import java.util.concurrent.ExecutorService;

public abstract class AbstractFilter implements Filter {


    @Override
    public void inactive(Chain chain, ChannelSlot target) {
        chain.fireInactive(target);
    }

    @Override
    public void receivedMessage(Chain chain, ChannelSlot target, Object message) {
        chain.fireReceivedMessage(target,message);
    }

    @Override
    public void exceptionCaught(Chain chain, ChannelSlot target, Throwable throwable) {
        chain.fireExceptionCaught(target,throwable);
    }

    @Override
    public void close(Chain chain, ChannelSlot target, Reason reason) {
        chain.fireClose(target,reason);
    }


}
