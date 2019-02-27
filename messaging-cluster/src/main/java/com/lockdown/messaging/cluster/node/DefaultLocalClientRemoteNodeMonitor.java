package com.lockdown.messaging.cluster.node;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.RegisterNature;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.exception.MessagingNoNodeException;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.RemoteNodeCommandHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultLocalClientRemoteNodeMonitor implements LocalClientRemoteNodeMonitor {

    private final MessagingNodeContext messagingNodeContext;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Bootstrap bootstrap;
    private RemoteNodeSlot forwardSlot;
    private Map<ServerDestination, RemoteNode> nodeContext = new ConcurrentHashMap<>();
    private Map<ChannelId, RemoteNode> invalidNodeContext = new ConcurrentHashMap<>();
    private Object lock = new Object();

    public DefaultLocalClientRemoteNodeMonitor(MessagingNodeContext messagingNodeContext) {
        this.messagingNodeContext = messagingNodeContext;
    }


    @Override
    public final void initLocalClient(InitializedCallback<LocalClientRemoteNodeMonitor> callback) {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NodeCommandDecoder(messagingNodeContext.getProperties().getNodePort()), new NodeCommandEncoder(messagingNodeContext.getProperties().getNodePort()));
                socketChannel.pipeline().addLast(new RemoteNodeCommandHandler(messagingNodeContext));
            }
        });
        if (Objects.nonNull(callback)) {
            callback.initialized(this);
        }
    }


    @Override
    public ChannelFuture connect(ServerDestination destination) {
        try {
            return bootstrap.connect(new InetSocketAddress(destination.getIpAddress(), destination.getPort())).sync();
        } catch (InterruptedException e) {
            throw new MessagingInterruptedException(e);
        }
    }

    @Override
    public RemoteNode getRemoteNode(ServerDestination destination) {
        RemoteNode remoteNode;
        synchronized (lock) {
            if (nodeContext.containsKey(destination)) {
                remoteNode = nodeContext.get(destination);
            } else {
                ChannelFuture channelFuture = connect(destination);
                remoteNode = newRemoteNodeInstance(channelFuture, destination);
                nodeContext.putIfAbsent(remoteNode.destination(), remoteNode);
            }
        }
        return remoteNode;
    }

    @Override
    public Collection<RemoteNode> AllRemoteNodes() {
        return nodeContext.values();
    }

    @Override
    public RemoteNode randomNode() {
        if (nodeContext.size() == 0) {
            throw new MessagingNoNodeException("没有更多节点!");
        }
        return getFirstNode();
    }

    private RemoteNode getFirstNode() {
        Optional<RemoteNode> optional = nodeContext.values().stream().findFirst();
        if (!optional.isPresent()) {
            throw new MessagingNoNodeException("没有更多节点!");
        }
        return optional.get();

    }

    @Override
    public void closeNode(RemoteNode remoteNode) {
        Objects.requireNonNull(remoteNode);
        RemoteNode serverNode = nodeContext.remove(remoteNode.destination());
        if (Objects.nonNull(serverNode)) {
            serverNode.close();
        }
    }


    @Override
    public RemoteNode newRemoteNodeInstance(ChannelFuture channelFuture) {
        return this.newRemoteNodeInstance(channelFuture, null);
    }

    @Override
    public RemoteNode newRemoteNodeInstance(ChannelFuture channelFuture, ServerDestination destination) {
        RemoteNode remoteNode = RemoteNodeFactory.getNodeProxyInstance(messagingNodeContext,channelFuture,destination);
        invalidNodeContext.putIfAbsent(channelFuture.channel().id(), remoteNode);
        return remoteNode;
    }

    @Override
    public void printNodes() {
        logger.info("当前节点 {}", JSON.toJSONString(nodeContext.keySet()));
    }


    @Override
    public void receivedCommand(RemoteNode remoteNode, SourceNodeCommand msg) {
        if (RegisterNature.class.isAssignableFrom(msg.getClass())) {
            applyDestination(remoteNode, msg.getSource());
        }
        forwardSlot.receivedCommand(remoteNode, msg);
    }

    @Override
    public void inactive(RemoteNode remoteNode) {
        RemoteNode origin = nodeContext.remove(remoteNode.destination());
        if (Objects.nonNull(origin)) {
            origin.close();
            forwardSlot.inactive(remoteNode);
        }
    }

    @Override
    public void exceptionCaught(RemoteNode remoteNode, Throwable cause) {
        inactive(remoteNode);
    }

    @Override
    public void applyDestination(RemoteNode remoteNode, ServerDestination destination) {
        ChannelId channelId = remoteNode.channelId();
        if (!invalidNodeContext.containsKey(channelId)) {
            return;
        }
        RemoteNode node = invalidNodeContext.remove(channelId);
        node.applyDestination(destination);
        nodeContext.putIfAbsent(destination, remoteNode);
    }


    @Override
    public void registerForwardSlot(RemoteNodeSlot slot) {
        if (Objects.isNull(slot)) {
            throw new IllegalArgumentException(" slot can't be null !");
        }
        if (Objects.nonNull(this.forwardSlot)) {
            throw new IllegalStateException((" slot already set !"));
        }
        this.forwardSlot = slot;
    }
}
