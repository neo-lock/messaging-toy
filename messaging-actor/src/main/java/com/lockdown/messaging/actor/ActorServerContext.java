package com.lockdown.messaging.actor;

import com.lockdown.messaging.cluster.ClusterServerContext;

public class ActorServerContext extends ClusterServerContext<ActorProperties> {


    public ActorServerContext(ActorProperties properties) {
        super(properties);
    }
}
