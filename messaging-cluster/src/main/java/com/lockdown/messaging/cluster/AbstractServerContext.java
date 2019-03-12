package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.channel.RemoteNodeChannelFactory;
import com.lockdown.messaging.cluster.channel.support.DefaultNodeChannelFactoryGroup;
import com.lockdown.messaging.cluster.channel.support.DefaultRemoteNodeChannelFactory;
import com.lockdown.messaging.cluster.node.*;
import com.lockdown.messaging.cluster.node.invoker.LocalNodeMessagingChannel;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
import com.lockdown.messaging.cluster.reactor.NodeChannelFactoryGroup;
import com.lockdown.messaging.cluster.reactor.support.DefaultChannelEventLoop;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;
import com.lockdown.messaging.cluster.support.SimpleRuntimeEnvironment;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public abstract class AbstractServerContext<T extends ServerProperties> implements ServerContext<T> {

    private final T properties;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Pattern nodeWhiteList;
    private ContextExecutor contextExecutor;
    private ServerDestination localDestination;
    private RuntimeEnvironment runtimeEnvironment;
    private DefaultChannelEventLoop eventLoop;
    private LocalNode localNode;
    private NodeChannelFactoryGroup channelGroup;


    public AbstractServerContext(T properties) {
        this.properties = properties;
        this.nodeWhiteList = Pattern.compile(this.properties.getNodeWhiteList());
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
        this.contextExecutor = new ContextExecutor(properties);
        this.runtimeEnvironment = new SimpleRuntimeEnvironment();
        LocalClient localClient = new ClusterLocalClient(this);
        this.eventLoop = new DefaultChannelEventLoop(this.contextExecutor.getSegment());
        RemoteNodeChannelFactory channelFactory = new DefaultRemoteNodeChannelFactory(this.eventLoop, localClient);
        this.channelGroup = new DefaultNodeChannelFactoryGroup(channelFactory);
        this.eventLoop.setNodeChannelGroup(channelGroup);
        this.localNode = new RecoverableLocalNodeFactory(this).getNodeInstance();
        LocalNodeMessagingChannel messagingChannel = new LocalNodeMessagingChannel(eventLoop,localNode);
        this.eventLoop.setLocalChannel(messagingChannel);
    }


    public final void startInitContext() {
        logger.info("start init necessary !");
        checkProperties();
        initNecessary();
        logger.info(" start init local node ");
        this.localNode = new ClusterLocalNode(this.localDestination, this.channelGroup);

    }

    protected abstract void checkProperties();

    protected abstract void initNecessary();

    @Override
    public Pattern nodeWhiteList() {
        return this.nodeWhiteList;
    }

    @Override
    public T getProperties() {
        return properties;
    }

    @Override
    public RuntimeEnvironment runtimeEnvironment() {
        return runtimeEnvironment;
    }

    @Override
    public ServerDestination localDestination() {
        return localDestination;
    }

    @Override
    public ContextExecutor contextExecutor() {
        return contextExecutor;
    }

    @Override
    public LocalNode localNode() {
        return this.localNode;
    }

    @Override
    public ChannelEventLoop channelEventLoop() {
        return this.eventLoop;
    }

    @Override
    public void serverStop(LocalServer localServer) {
        shutdownContext();
    }

    @Override
    public void shutdownContext() {
        this.eventLoop.shutdown();
        if (Objects.nonNull(contextExecutor)) {
            this.contextExecutor.shutdown();
        }
        if (Objects.nonNull(runtimeEnvironment)) {
            this.runtimeEnvironment.shutdown();
        }
    }

    @Override
    public void serverStartup(LocalServer localServer, ServerContext serverContext) {
        this.eventLoop.start();
        if (null != this.properties.getMaster() && !this.localDestination.equals(this.properties.getMaster())) {
            logger.info(" 需要连接的master {}", this.properties.getMaster());
            this.localNode.registerToCluster(this.properties.getMaster());
        } else {
            this.localNode.registerRandomNode();
        }
        this.startPrintNode(properties.isMonitorEnable());
    }

    private void startPrintNode(boolean enable) {
        if (enable) {
            long delay = properties.getMonitorSeconds() < 10 ? 10 : properties.getMonitorSeconds();
            runtimeEnvironment.newTimeout(new NodeMonitorDebug(delay, TimeUnit.SECONDS), delay, TimeUnit.SECONDS);
        }
    }

    private class NodeMonitorDebug implements TimerTask {


        private final long delay;
        private final TimeUnit timeUnit;

        NodeMonitorDebug(long delay, TimeUnit timeUnit) {
            this.delay = delay;
            this.timeUnit = timeUnit;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            localNode.printNodes();
            timeout.timer().newTimeout(this, delay, timeUnit);
        }
    }

}
