package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorDestination;
import org.springframework.data.annotation.Id;

public class ActorRecord {

    @Id
    private String accountId;
    private boolean connected;
    private ActorDestination actorDestination;

    public ActorRecord() {

    }

    public ActorRecord(ActorDestination actorDestination) {
        this.actorDestination = actorDestination;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public ActorDestination getActorDestination() {
        return actorDestination;
    }

    public void setActorDestination(ActorDestination actorDestination) {
        this.actorDestination = actorDestination;
    }

    @Override
    public String toString() {
        return "ActorRecord{" +
                "accountId='" + accountId + '\'' +
                ", connected=" + connected +
                ", actorDestination=" + actorDestination.toString() +
                '}';
    }
}
