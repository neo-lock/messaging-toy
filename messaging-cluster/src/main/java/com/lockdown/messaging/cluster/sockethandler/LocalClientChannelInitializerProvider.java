package com.lockdown.messaging.cluster.sockethandler;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.node.RemoteNodeBeanFactory;
import com.lockdown.messaging.cluster.sockethandler.ChannelInitializerProvider;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandDecoder;
import com.lockdown.messaging.cluster.sockethandler.NodeCommandEncoder;
import com.lockdown.messaging.cluster.sockethandler.RemoteNodeCommandHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class LocalClientChannelInitializerProvider implements ChannelInitializerProvider {

    private ServerContext serverContext;
    private RemoteNodeBeanFactory nodeBeanFactory;

    public LocalClientChannelInitializerProvider(RemoteNodeBeanFactory beanFactory, ServerContext serverContext) {
        this.nodeBeanFactory = beanFactory;
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
                    new NodeCommandDecoder(serverContext.getProperties().getNodePort()),
                    new NodeCommandEncoder(serverContext.getProperties().getNodePort()),
                    new SyncCommandHandler(serverContext),
                    new RemoteNodeCommandHandler(nodeBeanFactory));
        }
    }
}
