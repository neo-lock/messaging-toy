package com.lockdown.messaging.example.actor;

import com.lockdown.messaging.actor.ActorDestination;
import com.lockdown.messaging.actor.ActorProperties;
import com.lockdown.messaging.actor.ClusterActorServerContext;
import com.lockdown.messaging.actor.Actor;
import com.lockdown.messaging.actor.framework.ClusterActorMonitoringBeanFactory;
import com.lockdown.messaging.cluster.exception.MessagingException;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpringActorBeanFactory extends ClusterActorMonitoringBeanFactory {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private ApplicationContext applicationContext;
    private Map<String, FieldDefinition> injectMap = new HashMap<>();

    public SpringActorBeanFactory(ClusterActorServerContext<? extends ActorProperties> serverContext, ApplicationContext applicationContext) {
        super(serverContext);
        this.applicationContext = applicationContext;
        this.initInject(serverContext.getProperties().getActorClassName());
    }

    private void initInject(String actorClassName) {
        try {
            Class<?> actorClass = Class.forName(actorClassName);
            Field[] fields = actorClass.getDeclaredFields();
            for (Field f : fields) {
                if (Objects.nonNull(f.getAnnotation(Autowired.class))) {
                    f.setAccessible(true);
                    injectMap.put(f.getName(), new FieldDefinition(f, this.applicationContext.getBean(f.getType())));
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new MessagingException(ex);
        }

    }


    @Override
    public Actor getInstance(ChannelFuture channelFuture, ActorDestination destination) {
        Actor actor = super.getInstance(channelFuture, destination);
        injectMap.values().forEach(fieldDefinition -> {
            try {
                fieldDefinition.getField().set(actor, fieldDefinition.getValue());
            } catch (IllegalAccessException e) {
                throw new MessagingException(e);
            }
        });
        return actor;
    }

    private class FieldDefinition {
        private final Field field;
        private final Object value;

        public FieldDefinition(Field field, Object value) {
            this.field = field;
            this.value = value;
        }

        public Field getField() {
            return field;
        }

        public Object getValue() {
            return value;
        }
    }


}
