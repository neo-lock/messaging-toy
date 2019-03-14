package com.lockdown.messaging.cluster.channel;

public interface NodeChannelPipeline extends NodeChannelInbound, ChannelPipeline<NodeChannelHandler> {


    NodeChannelPipeline addLast(NodeChannelHandler handler);

    NodeChannel channel();

}
