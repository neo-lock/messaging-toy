package com.lockdown.messaging.cluster.support;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

public class SimpleRuntimeEnvironment implements RuntimeEnvironment {

    private MethodRecoverable methodRecoverable;
    private SyncCommandMonitor commandMonitor;
    private HashedWheelTimer hashedWheelTimer;

    public SimpleRuntimeEnvironment() {
        this.methodRecoverable = new TimerMethodRecoverable(this);
        this.commandMonitor = new DefaultSyncCommandMonitor();
        this.hashedWheelTimer = new HashedWheelTimer();
    }


    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        return hashedWheelTimer.newTimeout(task, delay, unit);
    }

    @Override
    public MethodRecoverable methodRecoverable() {
        return methodRecoverable;
    }

    @Override
    public SyncCommandMonitor syncCommandMonitor() {
        return commandMonitor;
    }

    @Override
    public void shutdown() {
        hashedWheelTimer.stop().forEach(Timeout::cancel);
    }
}
