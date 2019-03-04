package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorDestination;

import java.util.Collection;

public interface MessageService {


    void sendMessage(String accountId, String message);


    Collection<ActorDestination> allActors();

}
