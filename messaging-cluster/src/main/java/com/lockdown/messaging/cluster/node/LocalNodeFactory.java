package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.ServerDestination;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

public class LocalNodeFactory {

    public static LocalNode getNodeProxyInstance(MessagingNodeContext serverContext) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultLocalNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, new LocalServerNodeProxy(serverContext)});
        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setInterceptDuringConstruction(false);
        return (LocalNode) enhancer.create(new Class[]{LocalNodeCommandRouter.class,ServerDestination.class},new Object[]{serverContext.getCommandRouter(),serverContext.getLocalDestination()});
    }


    private static class ProxyCallbackFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            Recoverable recoverable = method.getAnnotation(Recoverable.class);
            if (null == recoverable) {
                return 0;
            } else {
                return 1;
            }
        }
    }

}
