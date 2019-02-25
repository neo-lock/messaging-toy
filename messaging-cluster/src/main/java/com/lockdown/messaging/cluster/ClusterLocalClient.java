package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.RemoteNodeCommandHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;


public class ClusterLocalClient implements LocalClient {

    private final MessagingNodeContext nodeContext;
    private Bootstrap bootstrap;

    public ClusterLocalClient(MessagingNodeContext nodeContext) {
        this.nodeContext = nodeContext;
        this.init();
    }

    private void init() {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NodeCommandDecoder(nodeContext.getProperties().getNodePort()), new NodeCommandEncoder(nodeContext.getProperties().getNodePort()));
                socketChannel.pipeline().addLast(new RemoteNodeCommandHandler(nodeContext.getEventDispatcher()));
            }
        });
    }


    @Override
    public ChannelFuture connect(ServerDestination source) {
        try {
            return bootstrap.connect(new InetSocketAddress(source.getIpAddress(),source.getPort())).sync();
        } catch (InterruptedException ex) {
            throw new MessagingInterruptedException(ex);
        }
    }

}
