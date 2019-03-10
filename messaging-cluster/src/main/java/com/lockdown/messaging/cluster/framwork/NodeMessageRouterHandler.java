package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.chain.AbstractFilter;
import com.lockdown.messaging.cluster.chain.Chain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMessageRouterHandler extends AbstractFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void receivedMessage(Chain chain, ChannelSlot target, Object message) {
        logger.info(" node router handler message !");
        chain.fireReceivedMessage(target,message);
    }

    @Override
    public void exceptionCaught(Chain chain, ChannelSlot target, Throwable throwable) {
        logger.info(" node router exception handler "+throwable.getMessage());
    }
}
