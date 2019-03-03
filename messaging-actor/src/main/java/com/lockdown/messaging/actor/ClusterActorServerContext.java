package com.lockdown.messaging.actor;

import com.lockdown.messaging.actor.framework.*;
import com.lockdown.messaging.cluster.ClusterServerContext;
import com.lockdown.messaging.cluster.node.ClusterLocalNode;
import com.lockdown.messaging.cluster.node.LocalNode;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;

import java.util.Objects;

public class ClusterActorServerContext extends ClusterServerContext<ActorProperties> implements ActorServerContext<ActorProperties> {

    private ClusterActorMonitor actorMonitor;
    private ActorMessageRouter actorMessageRouter;

    public ClusterActorServerContext(ActorProperties properties) {
        super(properties);
    }

    @Override
    protected void checkProperties() {
        super.checkProperties();
        if(StringUtil.isNullOrEmpty(this.properties.getActorClassName())){
            throw new IllegalArgumentException(" actor class name can't be empty !");
        }
        try {
            Class.forName(this.properties.getActorClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        if(StringUtil.isNullOrEmpty(this.properties.getActorCodecClassName())){
            throw new IllegalArgumentException(" actor codec class name can't be empty !");
        }
        try {
            Class.forName(this.properties.getActorCodecClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void initNecessary() {
        super.initNecessary();
        this.initActorMonitor();
        this.initActorMessageRouter();
    }

    @Override
    protected LocalNode initLocalNode() {
        ClusterLocalNode localNode = (ClusterLocalNode) super.initLocalNode();
        localNode.setCommandExecutor(new ActorNodeCommandExecutorFactory(this).getInstance());
        return localNode;
    }

    private void initActorMessageRouter() {
        if (Objects.isNull(this.actorMonitor)) {
            throw new IllegalArgumentException(" actor monitor not set !");
        }
        this.actorMessageRouter = new ClusterActorMessageRouter(this);
    }


    private void initActorMonitor() {
        ActorMonitoringBeanFactory beanFactory = new ClusterActorMonitoringBeanFactory(this);
        this.actorMonitor = new ClusterActorMonitor(beanFactory);
    }


    @Override
    public ActorMonitor actorMonitor() {
        return this.actorMonitor;
    }

    @Override
    public ActorMessageRouter actorMessageRouter() {
        return actorMessageRouter;
    }

    @Override
    public ActorBeanFactory actorBeanFactory() {
        return actorMonitor;
    }

    @Override
    public ActorDestination createActorDestination(Channel channel) {
        return new ActorDestination(localDestination(), channel.id().asLongText());
    }


}
