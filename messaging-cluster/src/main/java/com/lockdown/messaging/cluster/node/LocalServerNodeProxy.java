package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.exception.MessagingException;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


public class LocalServerNodeProxy implements MethodInterceptor {

    private final MessagingNodeContext serverContext;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public LocalServerNodeProxy(MessagingNodeContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Recoverable recoverable = method.getAnnotation(Recoverable.class);
        Object result = null;
        try {
            result = methodProxy.invokeSuper(o, args);
        } catch (Throwable ex) {
            logger.info(" 方法 {} 执行失败 {}，将重新尝试", method.toGenericString(),ex.getMessage());
            serverContext.registerRecoverable(method.toGenericString(), recoverable, o, methodProxy, args);
            if(recoverable.doThrow()){
                throw new MessagingException(ex);
            }
        }
        return result;
    }
}
