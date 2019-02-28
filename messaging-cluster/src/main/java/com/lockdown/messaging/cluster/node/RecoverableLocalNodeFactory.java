package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.support.Recoverable;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

public class RecoverableLocalNodeFactory implements LocalNodeFactory {


    private ProxyCallbackFilter proxyCallbackFilter = new ProxyCallbackFilter();
    private final ServerContext serverContext;

    public RecoverableLocalNodeFactory(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public LocalNode getNodeInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultLocalNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, new LocalServerNodeProxy(serverContext.runtimeEnvironment())});
        enhancer.setCallbackFilter(proxyCallbackFilter);
        enhancer.setInterceptDuringConstruction(false);
        return (LocalNode) enhancer.create(new Class[]{CommandRouter.class, ServerDestination.class},new Object[]{serverContext.commandRouter(),serverContext.localDestination()});
    }


    private  class ProxyCallbackFilter implements CallbackFilter {
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
