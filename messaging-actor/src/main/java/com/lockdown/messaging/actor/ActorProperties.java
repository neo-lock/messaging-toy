package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ServerProperties;

public interface ActorProperties extends ServerProperties {

    String getActorCodecClassName();

    String getActorClassName();

    String getActorFactoryClassName();

    int getActorPort();


}
