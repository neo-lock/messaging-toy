package com.lockdown.messaging.cluster.utils;

import com.lockdown.messaging.cluster.LocalServer;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.event.LocalServerEventListener;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GlobalTimer implements LocalServerEventListener {

    private   HashedWheelTimer wheelTimer = new HashedWheelTimer();

    @Override
    public void serverStartup(LocalServer localServer, MessagingNodeContext properties) {
        wheelTimer.start();
    }

    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit){
        return wheelTimer.newTimeout(task,delay,unit);
    }

    @Override
    public void serverStop(LocalServer localServer) {
        wheelTimer.stop();
    }



}
