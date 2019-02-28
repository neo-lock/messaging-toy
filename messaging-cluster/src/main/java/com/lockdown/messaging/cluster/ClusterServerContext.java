package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.node.*;
import com.lockdown.messaging.cluster.support.RuntimeEnvironment;
import com.lockdown.messaging.cluster.support.SimpleRuntimeEnvironment;
import com.lockdown.messaging.cluster.utils.IPUtils;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClusterServerContext implements ServerContext {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private final ClusterProperties properties;
    private ContextExecutor contextExecutor;
    private ServerDestination localDestination;
    private CommandRouter commandRouter;
    private NodeMonitor nodeMonitor;
    private LocalNode localNode;
    private RuntimeEnvironment runtimeEnvironment;

    public ClusterServerContext(ServerProperties properties) {
        logger.info("当前使用的配置:{}",properties);
        this.properties = (ClusterProperties) properties;
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
        this.contextExecutor = new ContextExecutor(properties);
        this.runtimeEnvironment = new SimpleRuntimeEnvironment();
    }


    @Override
    public void initContext() {
        this.initCommandRouter();
        this.initLocalNode();
    }

    private void initCommandRouter() {
        this.nodeMonitor = new SimpleNodeMonitorFactory(this).getInstance();
        this.commandRouter = new CommandSegmentRouter(nodeMonitor, this.contextExecutor);
        nodeMonitor.registerAcceptor(this.commandRouter);
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
        logger.info(" 需要连接的master {}",this.properties.getMaster());
        if(null!=this.properties.getMaster()&&!this.localDestination.equals(this.properties.getMaster())){
            this.localNode.registerToCluster(this.properties.getMaster());
        }else{
            this.localNode.registerRandomNode();
        }

        this.startPrintNode(properties.isMonitorEnable());
    }

    @Override
    public void serverStop(LocalServer localServer) {
        this.nodeMonitor.shutdown();
        this.contextExecutor.shutdown();
    }


    private void startPrintNode(boolean enable){
        if(enable){
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
    public ContextExecutor contextExecutor() {
        return contextExecutor;
    }

    @Override
    public RemoteNodeBeanFactory nodeBeanFactory() {
        return nodeMonitor;
    }

    @Override
    public CommandRouter commandRouter(){return commandRouter;}


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
