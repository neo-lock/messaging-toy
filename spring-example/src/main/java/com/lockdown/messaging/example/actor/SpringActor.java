package com.lockdown.messaging.example.actor;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.actor.AbstractActor;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.example.message.BusinessMessage;
import com.lockdown.messaging.example.message.CommunityMessage;
import com.lockdown.messaging.example.message.RegisterMessage;
import com.lockdown.messaging.example.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 每个socket都有一个actor,当前对象不处于 spring bean factory 里面!!
 */
public class SpringActor extends AbstractActor {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ActorRecord actorRecord = new ActorRecord();


    @Autowired
    private SpringActorService springActorService;

    @Override
    public void receivedMessage(ActorDestination sender, Object message) {
        logger.info(" received message {},{}",sender,JSON.toJSONString(message));
    }


    @Override
    public void receivedMessage(Object message) {
        if (BusinessMessage.class.isAssignableFrom(message.getClass())) {
            BusinessMessage businessMessage = (BusinessMessage) message;
            switch (businessMessage.getType()) {
                case REGISTER: {
                    RegisterMessage registerMessage = (RegisterMessage) message;
                    actorRecord.setActorDestination(getActorDestination());
                    actorRecord.setAccountId(registerMessage.getId());
                    actorRecord.setConnected(true);
                    springActorService.saveOrUpdate(actorRecord);
                    break;
                }
                case COMMUNITY: {
                    logger.info("收到community message {}", JSON.toJSONString(message));
                    CommunityMessage communityMessage = (CommunityMessage) message;
                    String actorId = communityMessage.getReceiverId();
                    ActorRecord record = springActorService.getActor(actorId);
                    logger.info("找到的actor {}",record);
                    if (null != record) {
                        writeMessage(record.getActorDestination(), new TextMessage(communityMessage.getText()),true);
                    }
                    break;
                }
                default: {
                    logger.info(" actor 收到Text消息 {}", JSON.toJSONString(message));
                }
            }
        }
    }


    @Override
    public void closedEvent() {
        actorRecord.setConnected(false);
        springActorService.updateActor(actorRecord);
    }

    @Override
    public void exceptionCaught(Throwable throwable) {
    }

}
