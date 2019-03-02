package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class LocalClientChannelInitializerProvider implements ChannelInitializerProvider {

    private ServerContext serverContext;

    public LocalClientChannelInitializerProvider(ServerContext serverContext) {
        this.serverContext = serverContext;
    }


    @Override
    public ChannelInitializer<SocketChannel> provideChannelInitializer() {
        return new LocalClientChannel(serverContext);
    }


    private class LocalClientChannel extends ChannelInitializer<SocketChannel> {

        private ServerContext serverContext;

        public LocalClientChannel(ServerContext serverContext) {
            this.serverContext = serverContext;
        }

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(
                    new NodeCommandDecoder(serverContext),
                    new NodeCommandEncoder(serverContext),
                    new SyncCommandHandler(serverContext),
                    new RemoteNodeCommandHandler(serverContext));
        }
    }
}
