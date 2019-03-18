package com.lockdown.messaging.example;

import com.lockdown.messaging.actor.AbstractActor;
import com.lockdown.messaging.actor.ActorFactory;
import com.lockdown.messaging.actor.ActorProperties;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.actor.channel.ActorChannel;
import com.lockdown.messaging.cluster.exception.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class SpringActorFactory implements ActorFactory {

    private Set<Field> fieldSet;

    public SpringActorFactory(ActorProperties actorProperties) {
        try {
            Class<?> actorClass = Class.forName(actorProperties.getActorClassName());
            this.initInjectField(actorClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new MessagingException(e);
        }
    }

    private void initInjectField(Class<?> actorClass){
        Field[] fields = actorClass.getFields();
        this.fieldSet = new HashSet<>();
        for(Field f : fields){
            if(null==f.getAnnotationsByType(Autowired.class)){
                continue;
            }
            f.setAccessible(true);
            fieldSet.add(f);
        }
    }

    @Override
    public AbstractActor newInstance(ActorChannel actorChannel) {
        Class<?> actorClass = ((ActorServerContext)actorChannel.eventLoop().serverContext()).actorClass();
        try {
            Object actor = actorClass.newInstance();
            injectField(actor);
            AbstractActor abstractActor = (AbstractActor) actor;
            abstractActor.setActorChannel(actorChannel);
            return abstractActor;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new MessagingException(e);
        }
    }

    private void injectField(Object actor) throws IllegalAccessException {
        for(Field field : fieldSet){
            Class<?> clazz = field.getType();
            field.set(actor,SpringBeanUtils.getBean(clazz));
        }
    }




}
