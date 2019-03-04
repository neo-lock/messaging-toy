package com.lockdown.messaging.example;

import com.lockdown.messaging.actor.ActorLocalServer;
import com.lockdown.messaging.actor.ClusterActorServerContext;
import com.lockdown.messaging.example.actor.SpringActorServer;
import com.lockdown.messaging.example.actor.SpringActorServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@EnableSwagger2
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class);
    }


    @Component
    public class ActorServerApplicationMonitor {

        private Logger logger = LoggerFactory.getLogger(getClass());

        @Autowired
        private SpringActorServer springActorServer;

        @Autowired
        private SpringActorServerContext springActorServerContext;

        @PostConstruct
        public void init() {
            logger.info(" server start !");
            springActorServerContext.startInitContext();
            springActorServer.initializer(springActorServerContext).start();
        }

        @PreDestroy
        public void destroy() {
            springActorServer.stop();
        }

    }

}
