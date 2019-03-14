package com.lockdown.messaging.cluster.channel;

public interface NodeChannelHandler extends ChannelHandler<NodeChannelContext> {

    void channelReceived(NodeChannelContext ctx, Object message);

    void channelClosed(NodeChannelContext ctx);

    void exceptionCaught(NodeChannelContext ctx, Throwable throwable);

}
