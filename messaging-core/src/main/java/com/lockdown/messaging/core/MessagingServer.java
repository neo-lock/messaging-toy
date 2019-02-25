package com.lockdown.messaging.core;
public interface MessagingServer {

    void start();

    void stop() throws InterruptedException;

    void triggerEvent() throws InterruptedException;
}
