package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.chain.AbstractFilter;
import com.lockdown.messaging.cluster.chain.Chain;
import com.lockdown.messaging.cluster.chain.Reason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeMonitorHandler extends AbstractFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void receivedMessage(Chain chain, ChannelSlot target, Object message) {
        logger.info(" node monitor handler message !");
        throw new RuntimeException("test");
        //chain.fireReceivedMessage(target,message);
    }

    @Override
    public void inactive(Chain chain, ChannelSlot target) {
        super.inactive(chain, target);
    }

    @Override
    public void exceptionCaught(Chain chain, ChannelSlot target, Throwable throwable) {
        logger.info(" node exception handler "+throwable.getMessage());
        chain.fireExceptionCaught(target,throwable);
    }


    @Override
    public void close(Chain chain, ChannelSlot target, Reason reason) {
        super.close(chain, target, reason);
    }
}
