package com.lockdown.messaging.cluster;

import com.alibaba.fastjson.JSON;
import com.lockdown.messaging.cluster.framwork.ClusterNodeMonitor;
import com.lockdown.messaging.cluster.framwork.MessageRouter;
import com.lockdown.messaging.cluster.framwork.NodeMessageAcceptor;
import com.lockdown.messaging.cluster.node.*;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;
import com.lockdown.messaging.cluster.support.SimpleRuntimeEnvironment;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ClusterServerContext<T extends ClusterProperties> extends AbstractServerContext<T> {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private ContextExecutor contextExecutor;
    private ServerDestination localDestination;
    private MessageRouter commandRouter;
    private ClusterNodeMonitor nodeMonitor;
    private LocalNode localNode;
    private RuntimeEnvironment runtimeEnvironment;

    public ClusterServerContext(T properties) {
        super(properties);
        logger.info(" 使用的properties :{}", JSON.toJSONString(properties));
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
        this.contextExecutor = new ContextExecutor(properties);
        this.runtimeEnvironment = new SimpleRuntimeEnvironment();
        this.initContext();
    }


    @Override
    public void initContext() {
        this.initCommandRouter();
        this.initLocalNode();
    }


    private void initCommandRouter() {
        this.nodeMonitor = new SimpleNodeMonitorFactory(this).getInstance();
        this.commandRouter = new MessageSegmentRouter(nodeMonitor, this.contextExecutor);
        nodeMonitor.registerAcceptor((MessageSegmentRouter) this.commandRouter);

    }



    private void initLocalNode() {
        localNode = new RecoverableLocalNodeFactory(this).getNodeInstance();
    }


    @Override
    public ServerDestination localDestination() {
        return localDestination;
    }


    @Override
    public void serverStartup(LocalServer localServer, ServerContext serverContext) {

        if (null != this.properties.getMaster() && !this.localDestination.equals(this.properties.getMaster())) {
            logger.info(" 需要连接的master {}", this.properties.getMaster());
            this.localNode.registerToCluster(this.properties.getMaster());
        } else {
            this.localNode.registerRandomNode();
        }

        this.startPrintNode(properties.isMonitorEnable());
    }

    @Override
    public void serverStop(LocalServer localServer) {
        if (Objects.nonNull(this.nodeMonitor)) {
            this.nodeMonitor.shutdown();
        }
        if (Objects.nonNull(this.contextExecutor)) {
            this.contextExecutor.shutdown();
        }
        if(null!=runtimeEnvironment){
            runtimeEnvironment.shutdown();
        }
    }


    private void startPrintNode(boolean enable) {
        if (enable) {
            long delay = properties.getMonitorSeconds() < 10 ? 10 : properties.getMonitorSeconds();
            runtimeEnvironment.newTimeout(new NodeMonitorDebug(delay, TimeUnit.SECONDS), delay, TimeUnit.SECONDS);
        }
    }


    @Override
    public ServerProperties getProperties() {
        return this.properties;
    }

    @Override
    public RuntimeEnvironment runtimeEnvironment() {
        return runtimeEnvironment;
    }

    @Override
    public void shutdownContext() {
        contextExecutor.shutdown();
    }

    @Override
    public ClusterNodeMonitor nodeMonitor() {
        return nodeMonitor;
    }

    @Override
    public ContextExecutor contextExecutor() {
        return contextExecutor;
    }



    @Override
    public MessageRouter commandRouter() {
        return commandRouter;
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
            nodeMonitor.printNodes();
            timeout.timer().newTimeout(this, delay, timeUnit);
        }
    }

}
