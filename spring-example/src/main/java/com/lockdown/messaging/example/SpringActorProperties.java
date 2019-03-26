package com.lockdown.messaging.example;


import com.lockdown.messaging.actor.SimpleActorProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "spring.actor")
public class SpringActorProperties extends SimpleActorProperties {

}
