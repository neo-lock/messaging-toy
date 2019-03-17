package com.lockdown.messaging.example;



import com.lockdown.messaging.actor.SimpleActorProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;


@ConfigurationProperties(prefix = "spring.actor")
public class SpringActorProperties extends SimpleActorProperties {

}
