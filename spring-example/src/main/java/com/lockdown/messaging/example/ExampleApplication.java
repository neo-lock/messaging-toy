package com.lockdown.messaging.example;

import com.lockdown.messaging.actor.*;
import com.lockdown.messaging.actor.channel.ActorChannelEventLoop;
import com.lockdown.messaging.example.message.JsonMessageDecoder;
import com.lockdown.messaging.example.message.JsonMessageEncoder;
import com.lockdown.messaging.example.message.SpringActorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;

@EnableSwagger2
@EnableConfigurationProperties(SpringActorProperties.class)
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class);
    }


    @Configuration
    public class ActorServerConfiguration {

        @Autowired
        private SpringActorProperties springActorProperties;

        @Primary
        @Bean
        public ActorFactory actorFactory() {
            return new SpringActorFactory(springActorProperties);
        }


        @Primary
        @Bean
        public ActorServerContext actorServerContext(ActorFactory actorFactory) {
            ActorServerContext actorServerContext = new ActorServerContext(springActorProperties);
            actorServerContext.setActorFactory(actorFactory);
            return actorServerContext;
        }
    }

    @Service
    public final class ActorServerApplication implements ActorServerUtils {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private ActorServer actorServer;

        @Autowired
        private ActorServerContext actorServerContext;

        @Autowired
        private SpringActorProperties springActorProperties;

        @PostConstruct
        public void init() {
            actorServer = new ActorServer(springActorProperties.getActorPort());
            actorServer.customHandler(serverContext -> Arrays.asList(
                    new JsonMessageDecoder(),
                    new JsonMessageEncoder(),
                    new SpringActorHandler()
            )).initializer(actorServerContext.check()).start();
        }

        public final void notifyMessage(Object message) {
            actorServerContext.notifyActorMessage(message);
        }

        @Override
        public final void pushMessage(ActorDestination destination, Object message) {
            actorServerContext.pushActorMessage(destination, message);
        }



        @PreDestroy
        public void destroy() {
            actorServer.stop();
        }

    }

}
