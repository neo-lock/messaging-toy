package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

public class LocalServerNodeFactory {

    public static LocalServerNode getNodeProxyInstance(MessagingNodeContext serverContext) {

        LocalServerNodeProxy proxy = new LocalServerNodeProxy(serverContext);
        ProxyCallbackFilter callbackFilter = new ProxyCallbackFilter();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultLocalServerNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, proxy});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setInterceptDuringConstruction(false);
        DefaultLocalServerNode serverNode = (DefaultLocalServerNode) enhancer.create();
        LocalNodeCommandRouter commandRouter = serverContext.getCommandRouter();
        serverNode.setCommandRouter(commandRouter);
        serverNode.setLocalDestination(serverContext.getLocalDestination());
        commandRouter.registerCommandAcceptor(serverNode);
        serverNode.initCommandExecutor();
        return serverNode;
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
