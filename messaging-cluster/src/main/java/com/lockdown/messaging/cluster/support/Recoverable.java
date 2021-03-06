package com.lockdown.messaging.cluster.support;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Recoverable {

    int intervalSeconds() default 5;


    int repeat() default 5;

    boolean doThrow() default false;


}
