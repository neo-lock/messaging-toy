package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.chain.AbstractFilterChain;

public class ChannelSlotHandler extends AbstractFilterChain  {


    public ChannelSlotHandler(){
        addFilter(new NodeMonitorHandler());
        addFilter(new NodeMessageRouterHandler());
    }




}
