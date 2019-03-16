package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.channel.ChannelInitializer;
import com.lockdown.messaging.cluster.channel.support.*;

public class LocalChannelInitializer implements ChannelInitializer<LocalChannel> {


    @Override
    public void initialize(LocalChannel channel) {
        channel.pipeline().addLast(new LocalNodeMasterHandler());
        channel.pipeline().addLast(new LocalNodeClosedHandler());
        channel.pipeline().addLast(new LocalNodeMonitoredHandler());
        channel.pipeline().addLast(new LocalNodeRegisterForwardHandler());
        channel.pipeline().addLast(new LocalNodeRegisterHandler());
    }

}
