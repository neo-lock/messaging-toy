package com.lockdown.messaging.actor;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.ServerDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestActor extends AbstractActor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void receivedMessage(ActorDestination destination, Object message) {
        logger.info("当前 actor {} 收到消息! {} {}",getActorDestination(),destination,JSON.toJSONString(message));
    }

    @Override
    public void receivedMessage(Object message) {
        logger.info("当前 actor {} 收到消息! {}", getActorDestination(),JSON.toJSONString(message));
        ActorDestination actorDestination = new ActorDestination("123",new ServerDestination("localhost",9091));
        writeMessage(actorDestination,message);
    }

    @Override
    public void closedEvent() {
        logger.info("当前 actor {}关闭!",getActorDestination());
    }

    @Override
    public void exceptionCaught(Throwable throwable) {
        logger.info("发生异常{}",throwable.getMessage());

    }


}
