package com.lockdown.messaging.actor.handler;
import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.sockethandler.LocalNodeCommandHandler;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.List;

public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel>
{



    private final ServerContext serverContext;

    protected AbstractChannelInitializer(ServerContext serverContext) {
        this.serverContext = serverContext;
    }


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
//        socketChannel.pipeline().addLast(new NodeCommandDecoder(serverContext.getProperties().getNodePort()), new NodeCommandEncoder(serverContext.getProperties().getNodePort()));
//        socketChannel.pipeline().addLast(new LocalNodeCommandHandler(serverContext));
//        socketChannel.pipeline().addLast(provideActorHandler().toArray(new ChannelHandler[0]));
//        socketChannel.pipeline().addLast(new ActorChannelHandler());
    }


    protected abstract List<ChannelHandler> provideActorHandler();

}
