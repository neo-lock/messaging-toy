package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.support.MessageSync;
import io.netty.channel.ChannelFuture;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;
import java.util.Objects;

public class MonitoringNodeBeanFactory extends AbstractRemoteNodeBeanFactory implements RemoteMonitoringNodeBeanFactory {

    private ProxyCallbackFilter callbackFilter = new ProxyCallbackFilter();
    private NodeMonitorSlot monitorSlot;

    MonitoringNodeBeanFactory(ServerContext serverContext) {
        super(serverContext);
    }

    @Override
    public void registerNodeMonitorSlot(NodeMonitorSlot monitorSlot) {
        this.monitorSlot = monitorSlot;
    }

    @Override
    public RemoteNode getNodeInstance(ChannelFuture channelFuture, ServerDestination destination) {
        Objects.requireNonNull(monitorSlot);
        RemoteNodeSyncProxy proxy = new RemoteNodeSyncProxy(getServerContext());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MonitoringNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, proxy});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setInterceptDuringConstruction(false);
        return (RemoteNode) enhancer.create(new Class[]{NodeMonitorSlot.class, ChannelFuture.class, ServerDestination.class},new Object[]{this.monitorSlot,channelFuture,destination});
    }

    @Override
    public RemoteNode getNodeInstance(ServerDestination destination) {
        return super.getNodeInstance(destination);
    }


    private static class ProxyCallbackFilter implements CallbackFilter {
        @Override
        public int accept(Method method) {
            MessageSync messageSync = method.getAnnotation(MessageSync.class);
            if (null == messageSync) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
