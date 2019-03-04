package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.node.LocalNode;
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

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private final T properties;
    private Pattern nodeWhiteList;
    private ContextExecutor contextExecutor;
    private ServerDestination localDestination;
    private RuntimeEnvironment runtimeEnvironment;
    private LocalNode localNode;


    public AbstractServerContext(T properties) {
        this.properties = properties;
        this.nodeWhiteList = Pattern.compile(this.properties.getNodeWhiteList());
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
        this.contextExecutor = new ContextExecutor(properties);
        this.runtimeEnvironment = new SimpleRuntimeEnvironment();
    }



    public final void startInitContext(){
        logger.info("start init necessary !");
        checkProperties();
        initNecessary();
        logger.info(" start init local node ");
        this.localNode =  initLocalNode();
    }

    protected abstract void checkProperties();

    protected abstract LocalNode initLocalNode();

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
        return localNode;
    }

    @Override
    public void serverStop(LocalServer localServer) {
        shutdownContext();
    }

    @Override
    public void shutdownContext() {
        if (Objects.nonNull(contextExecutor)) {
            this.contextExecutor.shutdown();
        }
        if (Objects.nonNull(runtimeEnvironment)) {
            this.runtimeEnvironment.shutdown();
        }
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
            nodeMonitor().printNodes();
            timeout.timer().newTimeout(this, delay, timeUnit);
        }
    }

}
