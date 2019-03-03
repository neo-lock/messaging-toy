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


    private final ServerContext serverContext;
    private ProxyCallbackFilter proxyCallbackFilter = new ProxyCallbackFilter();

    public RecoverableLocalNodeFactory(ServerContext serverContext) {
        this.serverContext = serverContext;
    }

    @Override
    public LocalNode getNodeInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ClusterLocalNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, new LocalServerNodeProxy(serverContext.runtimeEnvironment())});
        enhancer.setCallbackFilter(proxyCallbackFilter);
        enhancer.setInterceptDuringConstruction(false);
        ClusterLocalNode localNode = (ClusterLocalNode) enhancer.create(new Class[]{NodeMessageRouter.class, ServerDestination.class}, new Object[]{serverContext.commandRouter(), serverContext.localDestination()});
        localNode.setCommandExecutor(new ClusterNodeCommandExecutorFactory().getInstance());
        return localNode;
    }


    private class ProxyCallbackFilter implements CallbackFilter {
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
