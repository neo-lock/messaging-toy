package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.RemoteNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.SyncCommandHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterLocalClient implements LocalClient {


    private Bootstrap bootstrap;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ClusterLocalClient(ServerContext serverContext) {
        this.init(serverContext);
    }

    private void init(ServerContext serverContext) {
        bootstrap = new Bootstrap();
        bootstrap.group(serverContext.contextExecutor().getBoss());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(
                        new NodeCommandDecoder(serverContext),
                        new NodeCommandEncoder(serverContext),
                        new SyncCommandHandler(serverContext),
                        new RemoteNodeCommandHandler(serverContext));
            }
        });
    }


    @Override
    public ChannelFuture connect(ServerDestination source) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(source.getAddress(), source.getPort()).sync();
            logger.info("{} 连接成功!",source);
            return channelFuture;
        } catch (InterruptedException e) {
            throw new MessagingInterruptedException(e);
        }
    }


}

