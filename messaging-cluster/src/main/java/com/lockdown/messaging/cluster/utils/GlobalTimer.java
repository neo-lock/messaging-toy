package com.lockdown.messaging.cluster.utils;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

public class GlobalTimer {

    private HashedWheelTimer wheelTimer = new HashedWheelTimer();

    public GlobalTimer(){
        wheelTimer.start();
    }


    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        return wheelTimer.newTimeout(task, delay, unit);
    }

    public void stopTimer() {
        wheelTimer.stop();
    }


}
