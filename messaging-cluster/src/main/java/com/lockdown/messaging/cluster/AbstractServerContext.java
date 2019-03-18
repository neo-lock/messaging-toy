package com.lockdown.messaging.cluster;

import com.lockdown.messaging.cluster.command.*;
import com.lockdown.messaging.cluster.node.LocalNode;
import com.lockdown.messaging.cluster.reactor.ChannelEventLoop;
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
    private ChannelEventLoop eventLoop;
    private LocalNode localNode;

    public AbstractServerContext(T properties) {
        this.properties = properties;
        this.nodeWhiteList = Pattern.compile(this.properties.getNodeWhiteList());
        this.localDestination = new ServerDestination(IPUtils.getLocalIP(), properties.getNodePort());
        this.contextExecutor = new ContextExecutor(properties);
        this.runtimeEnvironment = new SimpleRuntimeEnvironment();
        this.init();
    }


    protected void init() {
        this.eventLoop = this.initEventLoop();
        this.localNode = this.eventLoop.localNode();
    }


    protected abstract ChannelEventLoop initEventLoop();


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
        //shutdownContext();
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
    public CommandCodecHandler codecHandler() {
        NodeCommandCodecHandler commandCodecHandler = new NodeCommandCodecHandler();
        commandCodecHandler.registerCodec(new CommandClosedCodec());
        commandCodecHandler.registerCodec(new CommandRegisterAskCodec());
        commandCodecHandler.registerCodec(new CommandMonitoredCodec());
        commandCodecHandler.registerCodec(new CommandGreetingCodec());
        commandCodecHandler.registerCodec(new CommandRegisterForwardCodec());
        return commandCodecHandler;
    }

    @Override
    public void serverStartup(LocalServer localServer, ServerContext serverContext) {
        this.eventLoop.start();
        logger.info(" 需要连接的master {}", this.properties.getMaster());
        if (null != this.properties.getMaster() && !this.localDestination.equals(this.properties.getMaster())) {

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
            eventLoop.nodeChannelGroup().printNodes();
            timeout.timer().newTimeout(this, delay, timeUnit);
        }
    }


}
