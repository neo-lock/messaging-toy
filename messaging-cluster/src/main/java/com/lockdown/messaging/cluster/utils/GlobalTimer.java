package com.lockdown.messaging.cluster.utils;

import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import io.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalTimer implements LocalServerEventListener {

    public static HashedWheelTimer wheelTimer = new HashedWheelTimer();
    private static Logger logger = LoggerFactory.getLogger(GlobalTimer.class);

    @Override
    public void serverStartup(LocalServer localServer, MessagingNodeContext properties) {
        logger.info(" timer start !");
        wheelTimer.start();
    }


    @Override
    public void serverStop(LocalServer localServer) {

        wheelTimer.stop();
        logger.info(" timer stop !");
    }


}
