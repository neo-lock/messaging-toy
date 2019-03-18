package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.AbstractActor;
import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.example.message.RegisterMessage;
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
        logger.info(" received message ");
    }


    @Override
    public void receivedMessage(Object message) {
        if (message instanceof RegisterMessage) {
            RegisterMessage registerMessage = (RegisterMessage) message;
            actorRecord.setActorDestination(getActorDestination());
            actorRecord.setAccountId(registerMessage.getId());
            actorRecord.setConnected(true);
            springActorService.saveOrUpdate(actorRecord);
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
