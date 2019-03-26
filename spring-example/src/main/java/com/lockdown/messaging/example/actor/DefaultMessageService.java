package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.example.ActorServerUtils;
import com.lockdown.messaging.example.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultMessageService implements MessageService {

    @Autowired
    private SpringActorService springActorService;

    @Autowired
    private ActorServerUtils actorServerUtils;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void pushMessage(MessageController.PushMessageRequest request) throws Exception {
        ActorRecord receiverRecord = springActorService.getActor(request.getReceiverId());
        actorServerUtils.pushMessage(receiverRecord.getActorDestination(), new TextMessage(request.getMessage()));
    }
}
