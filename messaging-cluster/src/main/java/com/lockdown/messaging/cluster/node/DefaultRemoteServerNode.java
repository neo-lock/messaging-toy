package com.lockdown.messaging.cluster.node;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.command.NodeCommand;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultRemoteServerNode extends AbstractServerNode implements RemoteServerNode {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final Channel channel;

    public DefaultRemoteServerNode(Channel channel) {
        this.channel = channel;
    }

    public DefaultRemoteServerNode(Channel channel, ServerDestination destination) {
        this(channel);
        this.destination = destination;
    }

    @Override
    public void sendCommand(NodeCommand command) {
        logger.info(" current remote  destination {}",this.destination);
        this.channel.writeAndFlush(command);
    }


    public void applyDestination(ServerDestination destination){
        if(null!=this.destination){
            return;
        }
        this.destination = destination;
    }


}
