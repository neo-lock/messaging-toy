package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.exception.MessagingException;
import com.lockdown.messaging.cluster.support.Recoverable;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


public class LocalServerNodeProxy implements MethodInterceptor {

    private final RuntimeEnvironment runtimeEnvironment;
    private Logger logger = LoggerFactory.getLogger(getClass());

    LocalServerNodeProxy(RuntimeEnvironment runtimeEnvironment) {
        this.runtimeEnvironment = runtimeEnvironment;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) {
        Recoverable recoverable = method.getAnnotation(Recoverable.class);
        Object result = null;
        try {
            result = methodProxy.invokeSuper(o, args);
        } catch (Throwable ex) {
            ex.printStackTrace();
            logger.info(" 方法 {} 执行失败 {}，将重新尝试", method.toGenericString(), ex.getMessage());
            runtimeEnvironment.methodRecoverable().registerRecoverable(method.toGenericString(), recoverable, o, methodProxy, args);
            if (recoverable.doThrow()) {
                throw new MessagingException(ex);
            }
        }
        return result;
    }
}
