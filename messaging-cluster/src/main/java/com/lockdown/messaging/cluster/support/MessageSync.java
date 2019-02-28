package com.lockdown.messaging.cluster.support;

import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.command.SyncCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageSync {

    int syncSeconds() default 10;

    boolean sync() default true;

    Class<? extends SourceNodeCommand> originParam();

    Class<? extends SyncCommand> convertTo();


}
