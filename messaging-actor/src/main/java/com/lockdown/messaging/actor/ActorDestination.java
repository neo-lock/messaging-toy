package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.Destination;
import com.lockdown.messaging.cluster.ServerDestination;

import java.util.Objects;

public class ActorDestination implements Destination {


    private final String channelId;
    private final ServerDestination destination;


    public ActorDestination(String channelId, ServerDestination destination) {
        this.channelId = channelId;
        this.destination = destination;
    }

    public String getChannelId() {
        return channelId;
    }

    public ServerDestination getDestination() {
        return destination;
    }

    @Override
    public String identifier() {
        return toString();
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

        return Objects.hash(channelId, destination);
    }

    @Override
    public String toString() {
        return "ActorDestination{" +
                "channelId='" + channelId + '\'' +
                ", destination=" + destination +
                '}';
    }
}
