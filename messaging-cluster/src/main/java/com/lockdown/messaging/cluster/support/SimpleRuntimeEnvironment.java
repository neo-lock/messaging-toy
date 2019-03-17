package com.lockdown.messaging.cluster.support;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SimpleRuntimeEnvironment implements RuntimeEnvironment {

    @Deprecated
    private MethodRecoverable methodRecoverable;
    @Deprecated
    private SyncCommandMonitor commandMonitor;
    private HashedWheelTimer hashedWheelTimer;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public SimpleRuntimeEnvironment() {
        this.methodRecoverable = new TimerMethodRecoverable(this);
        this.commandMonitor = new DefaultSyncCommandMonitor();
        this.hashedWheelTimer = new HashedWheelTimer();
    }


    @Override
    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
        return hashedWheelTimer.newTimeout(task, delay, unit);
    }

    @Deprecated
    @Override
    public MethodRecoverable methodRecoverable() {
        return methodRecoverable;
    }

    @Deprecated
    @Override
    public SyncCommandMonitor syncCommandMonitor() {
        return commandMonitor;
    }

    @Override
    public void shutdown() {
        hashedWheelTimer.stop().forEach(Timeout::cancel);
        logger.info(" timer shutdown !");
    }
}
