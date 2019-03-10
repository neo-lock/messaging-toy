package com.lockdown.messaging.cluster.framwork;

import com.lockdown.messaging.cluster.chain.AbstractFilter;
import com.lockdown.messaging.cluster.chain.Chain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorNodeMonitor extends AbstractFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void receivedMessage(Chain chain, ChannelSlot target, Object message) {
        //super.receivedMessage(chain, target, message);
        logger.info(" actor node monitor handler message !");
        //throw new RuntimeException("test");
        chain.fireReceivedMessage(target,message);

    }
}
