package com.lockdown.messaging.cluster.sockethandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public interface ChannelInitializerProvider {

    public ChannelInitializer<SocketChannel> provideChannelInitializer();

}
