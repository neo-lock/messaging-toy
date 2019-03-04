package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.ServerContext;
import com.lockdown.messaging.cluster.ServerDestination;
import com.lockdown.messaging.cluster.framwork.NodeMonitorUnit;
import com.lockdown.messaging.cluster.framwork.NodeMonitoringBeanFactory;
import com.lockdown.messaging.cluster.support.CommandEncode;
import com.lockdown.messaging.cluster.support.MessageSync;
import com.lockdown.messaging.cluster.support.RemoteNodeEncodeProxy;
import com.lockdown.messaging.cluster.support.RemoteNodeSyncProxy;
import io.netty.channel.ChannelFuture;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;
import java.util.Objects;

public class ClusterMonitoringNodeBeanFactory extends AbstractClusterNodeBeanFactory {

    private ProxyCallbackFilter callbackFilter = new ProxyCallbackFilter();
    private NodeMonitorUnit monitorSlot;
    private RemoteNodeSyncProxy proxy = new RemoteNodeSyncProxy(getServerContext());

    ClusterMonitoringNodeBeanFactory(ServerContext serverContext) {
        super(serverContext);
    }


    @Override
    public RemoteNode getInstance(ChannelFuture channelFuture, ServerDestination destination) {
        Objects.requireNonNull(monitorSlot);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ClusterRemoteNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE,proxy});
        enhancer.setCallbackFilter(callbackFilter);
        enhancer.setInterceptDuringConstruction(false);
        return (RemoteNode) enhancer.create(new Class[]{ChannelFuture.class, NodeMonitorUnit.class, ServerDestination.class}, new Object[]{channelFuture, this.monitorSlot, destination});
    }


    @Override
    void setMonitorUnit(NodeMonitorUnit monitorUnit) {
        this.monitorSlot = monitorUnit;
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
