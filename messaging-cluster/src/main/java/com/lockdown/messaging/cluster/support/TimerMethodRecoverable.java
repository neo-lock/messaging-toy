package com.lockdown.messaging.cluster.support;

import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerMethodRecoverable implements MethodRecoverable {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private final RuntimeEnvironment runtimeEnvironment;

    public TimerMethodRecoverable(RuntimeEnvironment runtimeEnvironment) {
        this.runtimeEnvironment = runtimeEnvironment;
    }


    @Override
    public void registerRecoverable(String methodName, Recoverable recoverable, Object target, MethodProxy methodProxy, Object[] args) {
        RecoverableDefinition definition = new RecoverableDefinition(methodName, recoverable.intervalSeconds(), recoverable.repeat());
        runtimeEnvironment.newTimeout(new RecoverableTask(definition, target, methodProxy, args), definition.getInterval(), TimeUnit.SECONDS);
    }


    private class RecoverableDefinition {
        private final String methodName;
        private final int interval;
        private final int repeat;

        RecoverableDefinition(String methodName, int interval, int repeat) {
            this.methodName = methodName;
            this.interval = interval;
            this.repeat = repeat;
        }


        String getMethodName() {
            return methodName;
        }

        int getInterval() {
            return interval;
        }

        int getRepeat() {
            return repeat;
        }
    }


    private class RecoverableTask implements TimerTask {

        private final RecoverableDefinition definition;
        private final Object invokeTarget;
        private final MethodProxy methodProxy;
        private final Object[] args;
        private AtomicInteger counter = new AtomicInteger(0);

        RecoverableTask(RecoverableDefinition definition, Object invokeTarget, MethodProxy methodProxy, Object[] args) {
            this.definition = definition;
            this.invokeTarget = invokeTarget;
            this.methodProxy = methodProxy;
            this.args = args;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            try {
                if (definition.getRepeat() != -1) {
                    if (counter.get() >= definition.getRepeat()) {
                        logger.warn(" 超过重试次数，将忽略执行方法 [{}]", definition.getMethodName());
                        return;
                    }
                }
                logger.debug(" {} 第 {} 遍执行 !", definition.getMethodName(), counter.get());
                methodProxy.invokeSuper(invokeTarget, args);
                logger.debug(" {} 执行成功 !", definition.getMethodName());
            } catch (Throwable throwable) {
                counter.incrementAndGet();
                timeout.timer().newTimeout(this, definition.getInterval(), TimeUnit.SECONDS);
            }
        }
    }


}
