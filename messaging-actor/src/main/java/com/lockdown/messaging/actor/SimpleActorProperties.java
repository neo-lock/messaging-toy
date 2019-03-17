package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ClusterProperties;

public class SimpleActorProperties extends ClusterProperties implements ActorProperties {


    private String actorMessageCodecClassName;
    private String actorClassName;

    @Override
    public String getActorCodecClassName() {
        return actorMessageCodecClassName;
    }

    public void setActorMessageCodecClassName(String actorMessageCodecClassName) {
        this.actorMessageCodecClassName = actorMessageCodecClassName;
    }

    @Override
    public String getActorClassName() {
        return actorClassName;
    }

    @Override
    public int getActorPort() {
        return 0;
    }

    public void setActorClassName(String actorClassName) {
        this.actorClassName = actorClassName;
    }
}
