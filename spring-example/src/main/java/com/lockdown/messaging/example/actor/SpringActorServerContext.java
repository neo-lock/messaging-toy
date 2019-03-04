package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.ClusterActorServerContext;
import com.lockdown.messaging.actor.framework.ActorMonitoringBeanFactory;
import com.lockdown.messaging.actor.framework.ClusterActorMonitor;
import com.lockdown.messaging.example.SpringMessagingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SpringActorServerContext extends ClusterActorServerContext<SpringMessagingProperties> implements ActorServerContext<SpringMessagingProperties> {


    private Logger logger = LoggerFactory.getLogger(getClass());


    public SpringActorServerContext(SpringMessagingProperties properties) {
        super(properties);
    }


    public void setActorBeanFactory(ActorMonitoringBeanFactory beanFactory) {
        setActorMonitor(new ClusterActorMonitor(beanFactory));
    }


    @Override
    protected void initNecessary() {
        super.initNecessary();
    }

    @Override
    protected void initActorMessageRouter() {
        super.initActorMessageRouter();
    }

    @Override
    protected void initActorMonitor() {
        Objects.requireNonNull(getActorMonitor());
    }
}
