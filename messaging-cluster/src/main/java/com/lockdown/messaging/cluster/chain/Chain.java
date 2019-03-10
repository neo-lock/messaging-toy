package com.lockdown.messaging.cluster.chain;

import com.lockdown.messaging.cluster.framwork.ChannelSlot;

public interface Chain extends Filter{


    void  addFilter(Filter filter);

    void fireInactive(ChannelSlot target);

    void fireReceivedMessage(ChannelSlot target,Object message);

    void fireExceptionCaught(ChannelSlot target,Throwable throwable);

    void fireClose(ChannelSlot target,Reason reason);


}
