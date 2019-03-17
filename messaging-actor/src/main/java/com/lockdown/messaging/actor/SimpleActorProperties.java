package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ClusterProperties;

public class SimpleActorProperties extends ClusterProperties implements ActorProperties {


    private String actorCodecClassName;
    private String actorClassName;
    private int actorPort;

    @Override
    public String getActorCodecClassName() {
        return actorCodecClassName;
    }

    public void setActorCodecClassName(String actorCodecClassName) {
        this.actorCodecClassName = actorCodecClassName;
    }

    @Override
    public String getActorClassName() {
        return actorClassName;
    }

    public void setActorClassName(String actorClassName) {
        this.actorClassName = actorClassName;
    }

    @Override
    public int getActorPort() {
        return actorPort;
    }

    public void setActorPort(int actorPort) {
        this.actorPort = actorPort;
    }
}
