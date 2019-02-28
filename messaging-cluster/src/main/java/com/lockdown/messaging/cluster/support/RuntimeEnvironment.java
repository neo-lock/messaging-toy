package com.lockdown.messaging.cluster.support;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

public interface RuntimeEnvironment {


    Timeout newTimeout(TimerTask task, long delay, TimeUnit unit);

    MethodRecoverable methodRecoverable();

    SyncCommandMonitor syncCommandMonitor();

    void shutdown();

}
