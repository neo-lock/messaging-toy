package com.lockdown.messaging.cluster.node;

import com.lockdown.messaging.cluster.MessagingNodeContext;
import com.lockdown.messaging.cluster.ServerDestination;
import io.netty.channel.ChannelFuture;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Method;

public class RemoteNodeFactory {


    public static RemoteNode getNodeProxyInstance(MessagingNodeContext serverContext,ChannelFuture channelFuture,ServerDestination destination){

        RemoteNodeSyncProxy proxy = new RemoteNodeSyncProxy(serverContext);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(DefaultRemoteNode.class);
        enhancer.setCallbacks(new Callback[]{NoOp.INSTANCE, proxy});
        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setInterceptDuringConstruction(false);
        return (RemoteNode) enhancer.create(new Class[]{RemoteNodeSlot.class,ChannelFuture.class,ServerDestination.class},new Object[]{serverContext.getRemoteNodeSlot(),channelFuture,destination});

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
