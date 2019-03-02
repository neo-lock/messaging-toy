package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ClusterProperties;

public class ActorProperties extends ClusterProperties {


    private int actorPort;

    public int getActorPort() {
        return actorPort;
    }

    public void setActorPort(int actorPort) {
        this.actorPort = actorPort;
    }
}
