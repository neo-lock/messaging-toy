package com.lockdown.messaging.cluster.node;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.command.NodeCommand;
import com.lockdown.messaging.cluster.command.SourceNodeCommand;
import com.lockdown.messaging.cluster.command.SyncCommand;
import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.exception.MessagingTimeoutException;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.*;


public class RemoteNodeSyncProxy implements MethodInterceptor {

    private final MessagingNodeContext serverContext;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public RemoteNodeSyncProxy(MessagingNodeContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        MessageSync sync = method.getAnnotation(MessageSync.class);
        int paramIndex = getOriginArgIndex(sync,args,method);
        if(paramIndex == -1 || !serverContext.isEnableSync() || !sync.sync()){
            return methodProxy.invokeSuper(o,args);
        }
        SyncCommand syncCommand = generateCommand(sync,args,paramIndex);
        CountDownLatch countDownLatch = serverContext.registerSyncMessage(syncCommand);
        args[paramIndex] = syncCommand;
        Future<Object>  future = serverContext.executeCallable(() -> {
            try {
                Object result1 =null;
                result1 = methodProxy.invokeSuper(o,args);
                countDownLatch.await(sync.syncSeconds(),TimeUnit.SECONDS);
                return result1;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new MessagingException(throwable);
            }
        });
        try {
            result = future.get(sync.syncSeconds(), TimeUnit.SECONDS);
        }catch (TimeoutException ex){
            throw new MessagingTimeoutException("消息"+syncCommand.toString()+"执行超时");
        }finally {
            serverContext.releaseSyncMessage(syncCommand.getCommandId());
        }

        return result;
    }



    private SyncCommand generateCommand(MessageSync sync,Object[] args,int paramIndex) throws IllegalAccessException, InstantiationException {
        if(null == args || args.length==0){
            throw new IllegalArgumentException(" parameters can't be empty !");
        }
        Object originParam = args[paramIndex];
        SyncCommand command = sync.convertTo().newInstance();
        command.setOriginCommand((SourceNodeCommand) originParam);
        command.setCommandId(UUID.randomUUID().toString().replace("-",""));
        return command;
    }

    //只支持一个参数
    private int getOriginArgIndex(MessageSync sync,Object[] args,Method method){
        if(args.length!=1){
            throw new IllegalArgumentException(" accept 1 param !");
        }
        Class<?>[] pTypes = method.getParameterTypes();
        if(!pTypes[0].isAssignableFrom(sync.convertTo())){
            throw new IllegalStateException(" convert to param type "+sync.convertTo()+" can't convert to "+pTypes[0]);
        }
        int paramIndex = -1;
        if(sync.originParam().isAssignableFrom(args[0].getClass())){
            paramIndex = 0;
        }
        return paramIndex;
    }




}
