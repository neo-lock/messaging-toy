package com.lockdown.messaging.cluster.event;

import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.MessagingNodeContext;

public interface LocalServerEventListener {


    public void serverStartup(LocalServer localServer, MessagingNodeContext properties);


    public void serverStop(LocalServer localServer);


}
