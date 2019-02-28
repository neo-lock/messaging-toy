package com.lockdown.messaging.cluster.event;

import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.ServerContext;

public interface ServerEventListener {


    void serverStartup(LocalServer localServer, ServerContext properties);


    void serverStop(LocalServer localServer);


}
