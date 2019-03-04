package com.lockdown.messaging.example.actor;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActorRepository extends MongoRepository<ActorRecord,String> {


    ActorRecord findByAccountIdAndConnected(String accountId,boolean connected);

    ActorRecord findByAccountId(String accountId);
}
