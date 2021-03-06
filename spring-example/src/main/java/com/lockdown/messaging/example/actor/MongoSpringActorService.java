package com.lockdown.messaging.example.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MongoSpringActorService implements SpringActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Override
    public void saveOrUpdate(ActorRecord actorRecord) {
        actorRepository.save(actorRecord);
    }


    @Override
    public ActorRecord getActor(String accountId) {
        return actorRepository.findByAccountIdAndConnected(accountId, true);
    }

    @Override
    public void updateActor(ActorRecord actorRecord) {
        actorRepository.save(actorRecord);
    }
}
