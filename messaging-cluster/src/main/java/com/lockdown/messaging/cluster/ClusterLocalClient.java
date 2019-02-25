package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.exception.MessagingInterruptedException;
import com.lockdown.messaging.cluster.sockethandler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class ClusterLocalClient implements LocalClient {

    private Bootstrap bootstrap;
    private final MessagingNodeContext nodeContext;

    public ClusterLocalClient(MessagingNodeContext nodeContext) {
        this.nodeContext = nodeContext;
        this.init();
    }

    private void init(){
        bootstrap = new Bootstrap();
        bootstrap.group(nodeContext.createEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NodeCommandDecoder(nodeContext.getProperties().getNodePort()), new NodeCommandEncoder(nodeContext.getProperties().getNodePort()));
                socketChannel.pipeline().addLast(new NodeIdleHandler(10, 10));
                socketChannel.pipeline().addLast(new RemoteNodeCommandHandler(nodeContext.getEventHandler()));
            }
        });
    }



    @Override
    public ChannelFuture connect(ServerDestination source){
        try{
            return bootstrap.connect(source.getIpAddress(),source.getPort()).sync();
        }catch (InterruptedException ex){
            throw new MessagingInterruptedException(ex);
        }
    }

}
