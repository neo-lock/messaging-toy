package com.lockdown.messaging.example.actor;

public interface SpringActorService {


    void saveOrUpdate(ActorRecord actorRecord);


    ActorRecord getActor(String accountId);


    void updateActor(ActorRecord actorRecord);


}
