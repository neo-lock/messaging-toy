package com.lockdown.messaging.cluster.reactor.support;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.channel.Channel;
import com.lockdown.messaging.cluster.channel.ChannelFactory;
import com.lockdown.messaging.cluster.channel.ChannelInitializer;
import com.lockdown.messaging.cluster.channel.support.NodeChannel;
import com.lockdown.messaging.cluster.channel.support.NodeCommandSplitter;
import com.lockdown.messaging.cluster.exception.MessagingHostException;
import com.lockdown.messaging.cluster.node.ClusterLocalClient;
import com.lockdown.messaging.cluster.node.LocalClient;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.NodeChannelGroup;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultChannelGroup implements NodeChannelGroup {

    private final Map<Destination, NodeChannel> channelMap = new ConcurrentHashMap<>();
    private final LocalClient localClient;
    private final ChannelEventLoop eventLoop;
    private final Object lock = new Object();
    private final ChannelInitializer<Channel> channelInitializer;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultChannelGroup(ServerContext serverContext, ChannelEventLoop eventLoop) {
        this.localClient = new ClusterLocalClient(serverContext);
        this.eventLoop = eventLoop;
        this.channelInitializer = new NodeChannelInitializer();
    }

    @Override
    public void addNodeChannel(NodeChannel channel) {
        channelMap.put(channel.destination(), channel);
    }

    @Override
    public void removeNodeChannel(Destination destination) {
        channelMap.remove(destination);
    }

    @Override
    public NodeChannel connectOnNotExists(Destination destination) {
        NodeChannel channel = null;
        synchronized (lock) {
            if (channelMap.containsKey(destination)) {
                channel = channelMap.get(destination);
            } else {
                ChannelFuture future = localClient.connect((ServerDestination) destination);
                channel = newInstance(future,(ServerDestination) destination);
                addNodeChannel(channel);
            }
        }
        return channel;
    }

    @Override
    public NodeChannel randomNodeChannel() {
        if (channelMap.isEmpty()) {
            throw new MessagingHostException("no host!");
        }
        Optional<NodeChannel> channelOptional = channelMap.values().stream().findFirst();
        if (!channelOptional.isPresent()) {
            throw new MessagingHostException("no host!");
        }
        return channelOptional.get();
    }

    @Override
    public Collection<NodeChannel> nodeChannels() {
        return channelMap.values();
    }

    @Override
    public NodeChannel getNodeChannel(Destination destination) {
        return channelMap.get(destination);
    }

    @Override
    public void printNodes() {
        logger.info("当前连接节点 {}",channelMap.keySet());
    }

    @Override
    public NodeChannel newInstance(ChannelFuture future, ServerDestination destination) {
        NodeChannel channel = new NodeChannel(eventLoop, destination, future);
        channelInitializer.initialize(channel);
        addNodeChannel(channel);
        return channel;
    }

    private class NodeChannelInitializer implements ChannelInitializer<Channel> {
        @Override
        public void initialize(Channel channel) {
            channel.pipeline().addLast(new NodeCommandSplitter());
        }
    }
}
