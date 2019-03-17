package com.lockdown.messaging.example;
import com.lockdown.messaging.actor.ActorServer;
import com.lockdown.messaging.actor.ActorServerContext;
import com.lockdown.messaging.example.message.JsonMessageDecoder;
import com.lockdown.messaging.example.message.JsonMessageEncoder;
import com.lockdown.messaging.example.message.SpringActorHandler;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

@EnableSwagger2
@EnableConfigurationProperties(SpringActorProperties.class)
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class);
    }


    @Component
    public class ActorServerApplication {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private ActorServer actorServer;

        private ActorServerContext actorServerContext;

        @Autowired
        private SpringActorProperties springActorProperties;

        @PostConstruct
        public void init() {
            actorServerContext = new ActorServerContext(springActorProperties);
            actorServer = new ActorServer(springActorProperties.getActorPort());
            actorServer.customHandler(serverContext -> Arrays.asList(
                    new JsonMessageDecoder(),
                    new JsonMessageEncoder(),
                    new SpringActorHandler()
            )).initializer(actorServerContext).start();

                    //.initializer(actorServerContext).start();
        }

        @PreDestroy
        public void destroy() {
            actorServer.stop();
        }

    }

}
