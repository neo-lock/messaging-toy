package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.framwork.*;
import com.lockdown.messaging.cluster.support.MessageSync;
import io.netty.channel.ChannelFuture;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;
import java.util.Objects;

public class ClusterMonitoringNodeBeanFactory extends AbstractClusterNodeBeanFactory implements NodeMonitoringBeanFactory {

    private ProxyCallbackFilter callbackFilter = new ProxyCallbackFilter();
    private NodeForwardSlot monitorSlot;

    ClusterMonitoringNodeBeanFactory(ServerContext serverContext) {
        super(serverContext);
    }


    @Override
    public RemoteNode getInstance(ChannelFuture channelFuture, ServerDestination destination) {
        Objects.requireNonNull(monitorSlot);
        RemoteNodeSyncProxy proxy = new RemoteNodeSyncProxy(getServerContext());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ClusterRemoteNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, proxy});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setInterceptDuringConstruction(false);
        return (RemoteNode) enhancer.create(new Class[]{ChannelFuture.class, NodeForwardSlot.class, ServerDestination.class}, new Object[]{channelFuture, this.monitorSlot, destination});
    }


    @Override
    public void setMonitorSlot(NodeForwardSlot forwardSlot) {
        this.monitorSlot = forwardSlot;
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