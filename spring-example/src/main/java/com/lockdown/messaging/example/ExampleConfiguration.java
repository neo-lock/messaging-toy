package com.lockdown.messaging.example;

import com.lockdown.messaging.actor.ActorLocalServer;
import com.lockdown.messaging.actor.ClusterActorServerContext;
import com.lockdown.messaging.example.actor.SpringActorBeanFactory;
import com.lockdown.messaging.example.actor.SpringActorServer;
import com.lockdown.messaging.example.actor.SpringActorServerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SpringMessagingProperties.class})
public class ExampleConfiguration {


    @Autowired
    private SpringMessagingProperties messagingProperties;


    @Bean
    public SpringActorServerContext springActorServerContext(ApplicationContext applicationContext) {
        SpringActorServerContext serverContext = new SpringActorServerContext(messagingProperties);
        serverContext.setActorBeanFactory(new SpringActorBeanFactory(serverContext, applicationContext));
        return serverContext;
    }

    @Bean
    public SpringActorServer actorLocalServer() {
        SpringActorServer localServer = new SpringActorServer();
        return localServer;
    }


}
