package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.example.message.JsonMessage;
import com.lockdown.messaging.example.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BaseMessageService implements MessageService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpringActorService springActorService;

    @Autowired
    private SpringActorServerContext springActorServerContext;

    @Override
    public void sendMessage(String accountId, String message) {
        ActorRecord actorRecord = springActorService.getActor(accountId);
        if(null != actorRecord){
            springActorServerContext.actorMonitor()
                    .findByDestination(actorRecord.getActorDestination())
                    .sendMessage(actorRecord.getActorDestination(), JsonMessage.wrap(new TextMessage(message)));
        }
    }

    @Override
    public Collection<ActorDestination> allActors() {
        return springActorServerContext.actorMonitor().allActors();
    }
}
