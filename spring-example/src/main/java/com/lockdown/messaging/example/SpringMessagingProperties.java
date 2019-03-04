package com.lockdown.messaging.example;

import com.lockdown.messaging.actor.ActorProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.messaging")
public class SpringMessagingProperties extends ActorProperties {

}
