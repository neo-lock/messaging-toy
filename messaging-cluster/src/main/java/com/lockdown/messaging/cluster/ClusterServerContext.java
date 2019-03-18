package com.lockdown.messaging.cluster;


import com.lockdown.messaging.cluster.command.*;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.DefaultChannelEventLoopInitializer;
import com.lockdown.messaging.cluster.reactor.support.DisruptorChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.support.NodeChannelInitializer;
import com.lockdown.messaging.cluster.reactor.support.RemoteNodeChannelInitializer;

public class ClusterServerContext extends AbstractServerContext<ClusterProperties> {


    public ClusterServerContext(ClusterProperties properties) {
        super(properties);
    }

    @Override
    protected ChannelEventLoop initEventLoop() {
        return new DisruptorChannelEventLoop(this, new DefaultChannelEventLoopInitializer());
    }


    @Override
    public NodeChannelInitializer nodeChannelInitializer() {
        return new RemoteNodeChannelInitializer();
    }

    @Override
    public ClusterServerContext check() {
        return this;
    }


}
