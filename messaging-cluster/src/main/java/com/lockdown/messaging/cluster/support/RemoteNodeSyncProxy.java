package com.lockdown.messaging.cluster.support;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.exception.MessagingTimeoutException;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Deprecated
public class RemoteNodeSyncProxy implements MethodInterceptor {

    private final ServerContext serverContext;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public RemoteNodeSyncProxy(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("执行sync!");
        Object result = null;
        MessageSync sync = method.getAnnotation(MessageSync.class);
        int paramIndex = getOriginArgIndex(sync, args, method);
        if (paramIndex == -1 || !serverContext.getProperties().isEnableSync() || !sync.sync()) {
            return methodProxy.invokeSuper(o, args);
        }
        SyncCommand syncCommand = generateCommand(sync, args, paramIndex);
        CountDownLatch countDownLatch = serverContext.runtimeEnvironment().syncCommandMonitor().monitorCommand(syncCommand);
        args[paramIndex] = syncCommand;
        Future<Object> future = getCommandFuture(countDownLatch, o, methodProxy, args, sync);
        try {
            result = future.get(sync.syncSeconds(), TimeUnit.SECONDS);
        } catch (TimeoutException ex) {
            throw new MessagingTimeoutException("消息" + syncCommand.toString() + "执行超时");
        } finally {
            logger.debug("释放监控消息{}", syncCommand);
            serverContext.runtimeEnvironment().syncCommandMonitor().releaseMonitor(syncCommand.getCommandId());
        }
        return result;
    }

    private Future<Object> getCommandFuture(CountDownLatch countDownLatch, Object invoker, MethodProxy methodProxy, Object[] args, MessageSync sync) {
        return serverContext.contextExecutor().getSegment().submit(() -> {
            try {
                Object result1 = null;
                result1 = methodProxy.invokeSuper(invoker, args);
                countDownLatch.await(sync.syncSeconds(), TimeUnit.SECONDS);
                return result1;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new MessagingException(throwable);
            }
        });
    }


    private SyncCommand generateCommand(MessageSync sync, Object[] args, int paramIndex) throws IllegalAccessException, InstantiationException {
        if (null == args || args.length == 0) {
            throw new IllegalArgumentException(" parameters can't be empty !");
        }
        Object originParam = args[paramIndex];
        SyncCommand command = sync.convertTo().newInstance();
        command.setOriginCommand((SourceNodeCommand) originParam);
        command.setCommandId(UUID.randomUUID().toString().replace("-", ""));
        return command;
    }

    //只支持一个参数
    private int getOriginArgIndex(MessageSync sync, Object[] args, Method method) {
        if (args.length != 1) {
            throw new IllegalArgumentException(" accept 1 param !");
        }
        Class<?>[] pTypes = method.getParameterTypes();
        if (!pTypes[0].isAssignableFrom(sync.convertTo())) {
            throw new IllegalStateException(" convert to param type " + sync.convertTo() + " can't convert to " + pTypes[0]);
        }
        int paramIndex = -1;
        if (sync.originParam().isAssignableFrom(args[0].getClass())) {
            paramIndex = 0;
        }
        return paramIndex;
    }


}
