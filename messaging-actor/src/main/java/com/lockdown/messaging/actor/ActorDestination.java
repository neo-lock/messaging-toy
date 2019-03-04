package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.framwork.Destination;

import java.util.Objects;

public class ActorDestination implements Destination {

    private String channelId;
    private ServerDestination serverDestination;

    public ActorDestination(){}

    public ActorDestination(ServerDestination destination, String channelId) {
        this.serverDestination = destination;
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public ServerDestination getServerDestination() {
        return serverDestination;
    }

    @Override
    public String toString() {
        return "ActorDestination{" +
                "channelId='" + channelId + '\'' +
                ", serverDestination=" + serverDestination.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorDestination that = (ActorDestination) o;
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverDestination,channelId);
    }

    @Override
    public String identifier() {
        return toString();
    }
}
