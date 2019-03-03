package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ClusterProperties;

public class ActorProperties extends ClusterProperties {


    private int actorPort;
    private String actorClassName;
    private String actorCodecClassName;

    public String getActorClassName() {
        return actorClassName;
    }

    public void setActorClassName(String actorClassName) {
        this.actorClassName = actorClassName;
    }

    public String getActorCodecClassName() {
        return actorCodecClassName;
    }

    public void setActorCodecClassName(String actorCodecClassName) {
        this.actorCodecClassName = actorCodecClassName;
    }

    public int getActorPort() {
        return actorPort;
    }

    public void setActorPort(int actorPort) {
        this.actorPort = actorPort;
    }
}
