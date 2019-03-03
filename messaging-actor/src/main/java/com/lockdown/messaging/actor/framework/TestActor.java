package com.lockdown.messaging.actor.framework;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.actor.ActorDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestActor extends AbstractActor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void receivedMessage(ActorDestination sender, Object message) {
        logger.info(" 收到服务器消息 {}", JSON.toJSON(message));
    }

    @Override
    public void receivedMessage(Object message) {
        logger.info("收到channel 消息 {}",JSON.toJSON(message));
    }

    @Override
    public void writeMessage(Object message) {

    }

}
