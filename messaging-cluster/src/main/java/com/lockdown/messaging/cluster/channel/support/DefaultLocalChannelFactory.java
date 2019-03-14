package com.lockdown.messaging.cluster.channel.support;
import com.lockdown.messaging.cluster.channel.LocalChannel;
import com.lockdown.messaging.cluster.channel.LocalChannelFactory;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;

public class DefaultLocalChannelFactory implements LocalChannelFactory {


    private final ChannelEventLoop eventLoop;
    private final LocalChannelInitializer channelInitializer;
    private final LocalNode localNode;

    public DefaultLocalChannelFactory(ChannelEventLoop eventLoop, LocalNode localNode) {
        this.eventLoop = eventLoop;
        this.localNode = localNode;
        this.channelInitializer = new LocalChannelInitializer();
    }


    @Override
    public LocalChannel newInstance() {
        DefaultLocalChannel channel = new DefaultLocalChannel(eventLoop, localNode);
        channelInitializer.initialize(channel);
        return channel;
    }
}
