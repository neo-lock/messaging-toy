package com.lockdown.messaging.core.event;
import com.lockdown.messaging.core.MessagingServer;

public interface MessagingServerEventListener {


    void serverStarted(MessagingServer messagingServer) throws InterruptedException;
}
